package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.*;
import ru.questsfera.questreservation.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TmpService {

    private final StatusRepository statusRepository;
    private final BlackListRepository blackListRepository;
    private final ClientRepository clientRepository;
    private final QuestRepository questRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Autowired
    public TmpService(StatusRepository statusRepository,
                      BlackListRepository blackListRepository,
                      ClientRepository clientRepository,
                      QuestRepository questRepository,
                      ReservationRepository reservationRepository,
                      UserRepository userRepository) {
        this.statusRepository = statusRepository;
        this.blackListRepository = blackListRepository;
        this.clientRepository = clientRepository;
        this.questRepository = questRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    //***Statuses
    @Transactional
    public void saveStatus(Quest quest, Status status) {
        quest.addStatusForQuest(status);
        statusRepository.save(status);
    }

    @Transactional
    public void deleteStatusForQuest(Quest quest, Status status) {
        status.deleteQuestForStatus(quest);
    }

    @Transactional
    public Set<Status> getStatusesByQuest(Quest quest) {
        return quest.getStatuses();
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

    //***Moderator
    @Transactional
    public User getUserById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new RuntimeException("Попытка получить несуществующего юзера");
    }

    @Transactional
    public Quest getQuestById(int id) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            return optionalQuest.get();
        }
        throw new RuntimeException("Попытка получить несуществующий квест");
    }

    @Transactional
    public void saveStatus(Status status) {
        statusRepository.save(status);
    }

    @Transactional
    public Status getStatusById(int id) {
        Optional<Status> optionalStatus = statusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            return optionalStatus.get();
        }
        throw new RuntimeException("Попытка получить несуществующий статус");
    }

    @Transactional
    public void deleteStatus(Status status) {
        if (status.getQuests().isEmpty()) {
            statusRepository.delete(status);
        } else {
            throw new RuntimeException("Попытка удалить статус к которому привязаны квесты");
        }
    }

    public Reservation getReserveById(int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        throw new RuntimeException("Попытка получить несуществующее бронирование");
    }

    public Quest getQuest(int id) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            return optionalQuest.get();
        }
        throw new RuntimeException("Попытка получить несуществующий квест");
    }

    public Client getClientById(int id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        throw new RuntimeException("Попытка получить несуществующего клиента");
    }

    public BlackList BlackListById(int id) {
        Optional<BlackList> optionalBlackList = blackListRepository.findById(id);
        if (optionalBlackList.isPresent()) {
            return optionalBlackList.get();
        }
        throw new RuntimeException("Попытка получить несуществующую запись в ЧС");
    }

    /*
    getAllAdmins
    getAdmin
    deleteAdmin
    getAllQuests
    deleteQuestWithReservations
    deleteEntryFromBlacklist
    addBlackListEntry
    getAllStatuses
    deleteClient
    deleteReservation
     */
}
