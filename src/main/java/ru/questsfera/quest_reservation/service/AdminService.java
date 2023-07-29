package ru.questsfera.quest_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.quest_reservation.dao.*;
import ru.questsfera.quest_reservation.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {
    private final AdminRepository adminRepository;;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ClientRepository clientRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository,
                        QuestRepository questRepository, StatusRepository statusRepository,
                        ClientRepository clientRepository, BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.statusRepository = statusRepository;
        this.clientRepository = clientRepository;
        this.blackListRepository = blackListRepository;
    }


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
        if (!admin.getQuests().contains(user)) {
            System.out.println("Попытка удалить юзер id:" + user.getId()
                    + " у админа id:" + admin.getId()
                    + " к которому юзер не относится. Или дублированный запрос");
            return;
        }

        admin.deleteUserForAdmin(user);
        userRepository.delete(user);
    }

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
        if (!admin.getQuests().contains(quest)) {
            System.out.println("Попытка удалить квест id:" + quest.getId()
                    + " у админа id:" + admin.getId()
                    + " к которому квест не относится. Или дублированный запрос");
            return;
        }

        admin.deleteQuestForAdmin(quest);
        if (!quest.getSynchronizedQuests().isEmpty()) {
            dontSynchronizeQuests(quest);
            System.out.println("Синхронизация по квесту id:" + quest.getId()
                    + " и всем связаным квестам отменена");
        }
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

    //******* synchronized quests
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

    //*********************************
    // *****BlackList
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
//        blackListRepository.save(blackList);
        client.setBlackList(blackList);
        clientRepository.save(client);
    }

    @Transactional
    public void deleteBlackList(Admin admin, Client client, BlackList blackList) {
        if (!admin.getBlackLists().contains(blackList)) {
            System.out.println("Попытка удалить запись из ЧС id:" + blackList.getId()
                    + " у админа id:" + admin.getId()
                    + " к которому запсь ЧС не относится. Или дублированный запрос");
            return;
        }

        client.deleteBlackListForClient();
        clientRepository.save(client);
        admin.deleteBlackListForAdmin(blackList);
        blackListRepository.delete(blackList);
    }

    // ******Clients
    @Transactional
    public Set<Client> getClientsByAdmin(Admin admin) {
        return admin.getClients();
    }
}
