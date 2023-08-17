package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.dao.*;
import ru.questsfera.questreservation.entity.Quest;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.util.LinkedList;

@Service
public class ClientService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ClientService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public LinkedList<Reservation> getReservationsByDate(Quest quest, LocalDate date) {
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
