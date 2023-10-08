package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.repository.*;
import ru.questsfera.questreservation.entity.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final StatusRepository statusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final BlackListRepository blackListRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        StatusRepository statusRepository,
                        ReservationRepository reservationRepository,
                        ClientRepository clientRepository,
                        BlackListRepository blackListRepository) {
        this.adminRepository = adminRepository;
        this.statusRepository = statusRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.blackListRepository = blackListRepository;
    }

    //***AdminService
    @Transactional
    public Admin getAdminByName(String username) {
        Optional<Admin> adminOptional = adminRepository.findAdminByUsername(username);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", username));
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
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


    //***Reservations
    @Transactional
    public LinkedList<Reservation> getReservationsByDate(Quest quest, LocalDate date) {
        return reservationRepository.findAllByQuestAndDateReserveOrderByTimeReserve(quest, date);
    }

    @Transactional
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation getReserveById(int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.orElse(null);
    }

    @Transactional
    public void deleteBlockedReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}
