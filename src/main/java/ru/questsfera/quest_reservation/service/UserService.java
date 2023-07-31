package ru.questsfera.quest_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.quest_reservation.dao.*;
import ru.questsfera.quest_reservation.entity.Quest;
import ru.questsfera.quest_reservation.entity.Reservation;
import ru.questsfera.quest_reservation.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public UserService(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;

    }

    @Transactional
    public User getUserById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new RuntimeException("Попытка получить несуществующего юзера");
    }

    @Transactional
    public List<Reservation> getReservationsByDate(User user, Quest quest, LocalDate date) {
        if (!quest.getAdmin().equals(user.getAdmin())) {
            throw new RuntimeException("Попытка получить бронирования недоступные"
                    + " для данного пользователя");
        }
        return reservationRepository.findAllByQuestAndDateReserve(quest, date);
    }

    @Transactional
    public void saveReservation(User user, Reservation reservation) {
        System.out.println(user.getQuests().size());
        if(!user.getQuests().contains(reservation.getQuest())) {
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
