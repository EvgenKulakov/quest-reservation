package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dto.Account;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation getReserveById(int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        throw new RuntimeException("Данного бронирования не существует");
    }

    @Transactional
    public LinkedList<Reservation> getReservationsByDate(Quest quest, LocalDate date) {
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Reservation reservation, Account account) {
        checkSecurityForReserve(reservation, account);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteBlockedReservation(Reservation reservation, Account account) {
        checkSecurityForReserve(reservation, account);
        reservationRepository.delete(reservation);
    }

    @Transactional
    public void checkSecurityForReserve(Reservation reservation, Account account) {
        if (!account.getQuests().contains(reservation.getQuest())) {
            throw new SecurityException("Нет доступа для редактирования данного бронирования");
        }
    }

    @Transactional
    public void doubleCheck(Reservation reservation) {

        boolean existsReservation = reservationRepository.existsByQuestAndDateReserveAndTimeReserve(
                reservation.getQuest(),
                reservation.getDateReserve(),
                reservation.getTimeReserve()
        );

        if (existsReservation) {
            throw new RuntimeException("Два бронирования на одно и тоже время");
        }
    }
}
