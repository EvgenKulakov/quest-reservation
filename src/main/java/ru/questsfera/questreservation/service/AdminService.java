package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.repository.*;
import ru.questsfera.questreservation.entity.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        QuestRepository questRepository,
                        StatusRepository statusRepository,
                        ReservationRepository reservationRepository,
                        ClientRepository clientRepository,
                        BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.questRepository = questRepository;
        this.statusRepository = statusRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.blackListRepository = blackListRepository;
    }

    //***AdminService
    @Transactional
    public Admin getAdminByName(String username) {
        Optional<Admin> adminOptional = adminRepository.findAdminByUsername(username);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", username));
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    //***Quests
    @Transactional
    public Quest getQuestById(int id) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            return optionalQuest.get();
        }
        throw new RuntimeException("Попытка получить несуществующий квест");
    }

    @Transactional
    public boolean existQuestName(Quest quest) {
        return questRepository.existsQuestByQuestNameAndAdmin(quest.getQuestName(), quest.getAdmin());
    }

    @Transactional
    public void saveQuest(Admin admin, Quest quest) {
        admin.addQuestForAdmin(quest);
        quest.saveUsers();
        questRepository.save(quest);
    }

    @Transactional
    public boolean hasReservations(Quest quest) {
        return reservationRepository.existsByQuest(quest);
    }

    @Transactional
    public void deleteQuest(Admin admin, Quest quest) {
        if (!quest.getAdmin().equals(admin)) {
            throw new RuntimeException("Попытка удалить квест админом, у которого нет доступа");
        }

        reservationRepository.deleteByQuest(quest);

        for (User user : quest.getUsers()) {
            user.deleteQuestForUser(quest);
        }

        for (Status status : quest.getStatuses()) {
            quest.deleteStatusForQuest(status);
        }

        if (!quest.getSynchronizedQuests().isEmpty()) {
            dontSynchronizeQuests(quest);
            System.out.println("Синхронизация по квесту id:" + quest.getId()
                    + " и всем связаным квестам отменена");
        }
        admin.deleteQuestForAdmin(quest);
        questRepository.delete(quest);
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
    public LinkedList<Reservation> getReservationsByDate(Quest quest, LocalDate date) {
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation getReserveById(int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.orElse(null);
    }

    @Transactional
    public void deleteBlockedReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}
