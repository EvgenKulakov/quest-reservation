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

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BlackListRepository blackListRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccountRepository accountRepository;

    //***Statuses
    @Transactional
    public void saveStatus(Quest quest, Status status) {
//        quest.addStatusForQuest(status);
        statusRepository.save(status);
    }

    @Transactional
    public void deleteStatusForQuest(Quest quest, Status status) {
//        status.deleteQuestForStatus(quest);
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
    public Set<BlackList> getBlackListsByCompany(Company company) {
        return company.getBlackLists();
    }

    @Transactional
    public void saveBlackList(Company company, Client client, BlackList blackList) {
        if (!client.getCompany().equals(company)) {
            throw new RuntimeException("Попытка создать запись в ЧС для клиента "
                    + "привязанного к другой компании");
        }
//        company.addBlackListForAdmin(blackList);
        client.setBlackList(blackList);
        clientRepository.save(client);
    }

    @Transactional
    public void deleteBlackList(Company company, Client client) {
        if (!client.getCompany().equals(company)) {
            throw new RuntimeException("Попытка удалить запись ЧС админом, "
                    + "у которого нет доступа к клиенту");
        }
        blackListRepository.delete(client.getBlackList());
//        client.deleteBlackListForClient();
    }

    //***Clients
    @Transactional
    public Set<Client> getClientsByCompany(Company company) {
        return company.getClients();
    }

    @Transactional
    public Client getClientByReserve(Reservation reservation) {
        return reservation.getClient();
    }

    //***Moderator
    @Transactional
    public Account getUserById(Integer id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        throw new RuntimeException("Попытка получить несуществующий аккаунт");
    }

    @Transactional
    public Quest getQuestById(Integer id) {
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
    public Status getStatusById(Integer id) {
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

    public Reservation getReserveById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        throw new RuntimeException("Попытка получить несуществующее бронирование");
    }

    public Quest getQuest(Integer id) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            return optionalQuest.get();
        }
        throw new RuntimeException("Попытка получить несуществующий квест");
    }

    public Client getClientById(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        throw new RuntimeException("Попытка получить несуществующего клиента");
    }

    public BlackList BlackListById(Integer id) {
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
