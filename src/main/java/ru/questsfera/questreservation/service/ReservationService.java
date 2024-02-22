package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Company;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.repository.ClientRepository;
import ru.questsfera.questreservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Reservation getReserveById(Long id) {
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
    public void saveReservation(Reservation reservation) {
//        checkSecurityForReserve(reservation, account);
        clientRepository.save(reservation.getClient());
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteBlockedReservation(Reservation reservation) {
//        checkSecurityForReserve(reservation, account);
        reservationRepository.delete(reservation);
    }

    @Transactional
    public void checkSecurityForReserve(Reservation reservation, Company company) {
        if (!company.getQuests().contains(reservation.getQuest())) {
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
