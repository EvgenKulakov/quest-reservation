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
import java.util.List;

@Service
public class ClientService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final StatusRepository statusRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public ClientService(AdminRepository adminRepository, UserRepository userRepository,
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

    //**************************************************
    @Transactional
    public List<Reservation> getReservationsByDate(Quest quest, Date date) {
        return reservationRepository.findAllByQuestAndDateReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Client client, Reservation reservation) {
        client.addReserveForClient(reservation);
        reservationRepository.save(reservation);
    }
}
