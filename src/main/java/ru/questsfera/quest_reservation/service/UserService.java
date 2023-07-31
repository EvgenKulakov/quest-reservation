package ru.questsfera.quest_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.quest_reservation.dao.*;
import ru.questsfera.quest_reservation.entity.Client;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;
import ru.questsfera.quest_reservation.entity.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ClientRepository clientRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public UserService(AdminRepository adminRepository, UserRepository userRepository,
                       ReservationRepository reservationRepository, QuestRepository questRepository,
                       StatusRepository statusRepository, ClientRepository clientRepository,
                       BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.questRepository = questRepository;
        this.statusRepository = statusRepository;
        this.clientRepository = clientRepository;
        this.blackListRepository = blackListRepository;
    }

    // ********************************
    @Transactional
    public User getUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new RuntimeException("Попытка получить несуществующего юзера");
    }

    @Transactional
    public List<Reservation> getReservationsByDate(Quest quest, User user, LocalDate date) {
        if (!quest.getAdmin().equals(user.getAdmin())) {
            throw new RuntimeException("Попытка получить бронирования недоступные"
                    + " для данного пользователя");
        }
        return reservationRepository.findAllByQuestAndDateReserve(quest, date);
    }

    @Transactional
    public void saveReservation(User user, Reservation reservation) {
        if (!reservation.getQuest().getUsers().contains(user)) {
            throw new RuntimeException("Попытка создать бронирование пользователем,"
                    + " у которого нет доступа к квесту");
        }
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation getReserveById(User user, int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            if (!reservation.getQuest().getUsers().contains(user)) {
                throw new RuntimeException("Попытка открыть бронирование пользователем,"
                        + " у которого нет доступа к квесту");
            }
            return reservation;
        }
        throw new RuntimeException("Попытка получить несуществующее бронирование");
    }
}
