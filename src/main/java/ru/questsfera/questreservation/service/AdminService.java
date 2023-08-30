package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.repository.*;
import ru.questsfera.questreservation.entity.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository,
                        QuestRepository questRepository, StatusRepository statusRepository,
                        ReservationRepository reservationRepository,
                        ClientRepository clientRepository,
                        BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.statusRepository = statusRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.blackListRepository = blackListRepository;
    }

    //***login
    @Transactional
    public Admin getAdminById(int id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }
        throw new RuntimeException("Попытка получить несуществующего админа");
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    //***Users
    @Transactional
    public Set<User> getUsersByAdmin(Admin admin) {
        return admin.getUsers();
    }

    @Transactional
    public void saveUser(Admin admin, User user) {
        admin.addUserForAdmin(user);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Admin admin, User user) {
        if (!user.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка удалить юзера админом, " +
                    "у которого нет доступа");
        }
        admin.deleteUserForAdmin(user);
        userRepository.delete(user);
    }

    //***Quests
    @Transactional
    public Set<Quest> getQuestsByAdmin(Admin admin) {
        return admin.getQuests();
    }

    @Transactional
    public void saveQuest(Admin admin, Quest quest) {
        admin.addQuestForAdmin(quest);
        questRepository.save(quest);
    }

    @Transactional
    public void deleteQuest(Admin admin, Quest quest) {
        if (!quest.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка удалить квест админом, у которого нет доступа");
        }

        if (!reservationRepository.findAllByQuest(quest).isEmpty()) {
            throw new RuntimeException("Попытка удалить квест, у которого есть бронирования");
        }

        if (!quest.getSynchronizedQuests().isEmpty()) {
            dontSynchronizeQuests(quest);
            System.out.println("Синхронизация по квесту id:" + quest.getId()
                    + " и всем связаным квестам отменена");
        }
        admin.deleteQuestForAdmin(quest);
        questRepository.delete(quest);
    }

    @Transactional
    public Set<Quest> getQuestsByUser(User user) {
        return user.getQuests();
    }

    @Transactional
    public void addQuestForUser(User user, Quest quest) {
        user.addQuestForUser(quest);
    }

    @Transactional
    public void deleteQuestForUser(User user, Quest quest) {
        user.deleteQuestForUser(quest);
    }

    //***Statuses
    @Transactional
    public void saveStatus(Quest quest, Status status) {
        quest.addStatusForQuest(status);
        statusRepository.save(status);
    }

    @Transactional
    public void deleteStatusForQuest(Quest quest, Status status) {
        quest.deleteStatusForQuest(status);
    }

    @Transactional
    public Set<Status> getStatusesByQuest(Quest quest) {
        return quest.getStatuses();
    }

    //***SynchronizeQuests
    @Transactional
    public void synchronizeQuests(Quest... quests) {
        Quest.synchronizeQuests(quests);
    }

    @Transactional
    public Set<Quest> getSynchronizedQuests(Quest quest) {
        return quest.getSynchronizedQuests();
    }

    @Transactional
    public void dontSynchronizeQuests(Quest quest) {
        Quest.dontSynchronizeQuests(quest);
    }

    //***BlackList
    @Transactional
    public List<BlackList> getAllBlackLists() {
        return blackListRepository.findAll();
    }

    @Transactional
    public Set<BlackList> getBlackListsByAdmin(Admin admin) {
        return admin.getBlackLists();
    }

    @Transactional
    public void saveBlackList(Admin admin, Client client, BlackList blackList) {
        if (!client.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка создать запись в ЧС для клиента "
                    + "привязанного к другому админу");
        }
        admin.addBlackListForAdmin(blackList);
        client.setBlackList(blackList);
        clientRepository.save(client);
    }

    @Transactional
    public void deleteBlackList(Admin admin, Client client) {
        if (!client.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка удалить запись ЧС админом, "
                    + "у которого нет доступа к клиенту");
        }
        blackListRepository.delete(client.getBlackList());
        client.deleteBlackListForClient();
    }

    //***Clients
    @Transactional
    public Set<Client> getClientsByAdmin(Admin admin) {
        return admin.getClients();
    }

    @Transactional
    public Client getClientByReserve(Reservation reservation) {
        return reservation.getClient();
    }


    //***Reservations
    @Transactional
    public LinkedList<Reservation> getReservationsByDate(Admin admin, Quest quest, LocalDate date) {
        if (!quest.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка получить бронирования недоступные"
                    + " для данного аккаунта");
        }
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Admin admin, Reservation reservation) {
        if (!reservation.getQuest().getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка создать бронирование аккаунтом,"
                    + " у которого нет доступа к квесту");
        }
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation getReserveById(Admin admin, int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            if (!admin.getQuests().contains(reservation.getQuest())) {
                throw new RuntimeException("Попытка открыть бронирование аккаунтом,"
                        + " у которого нет доступа к квесту");
            }
            return reservation;
        }
        throw new RuntimeException("Попытка получить несуществующее бронирование");
    }

    @Transactional
    public void deleteBlockedReservation(Admin admin, Reservation reservation) {
        if (!reservation.getQuest().getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка удалить бронирование без права доступа");
        }
        reservationRepository.delete(reservation);
        clientRepository.delete(reservation.getClient());
    }
}
