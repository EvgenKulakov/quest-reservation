package ru.questsfera.quest_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.quest_reservation.dao.*;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;
import ru.questsfera.quest_reservation.entity.Status;
import ru.questsfera.quest_reservation.entity.User;

import java.util.Optional;

@Service
public class ModeratorService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public ModeratorService(AdminRepository adminRepository, UserRepository userRepository,
                            QuestRepository questRepository, StatusRepository statusRepository,
                            ClientRepository clientRepository, ReservationRepository reservationRepository, BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.statusRepository = statusRepository;
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.blackListRepository = blackListRepository;
    }

    /*
    getAllAdmins
    getAdmin
    deleteAdmin
    getAllQuests
    deleteEntryFromBlacklist
    addBlackListEntry
    getAllStatuses
    getClientById
    deleteClient
    deleteReservation
     */

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
}
