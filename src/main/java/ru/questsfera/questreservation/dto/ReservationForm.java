package ru.questsfera.questreservation.dto;

import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;

public class ReservationForm {
    private Admin admin;
    private Reservation reservation;
    private StatusType statusType;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private Integer countPersons;
    private String adminComment;
    private String clientComment;

    public ReservationForm(Reservation reservation) {
        this.reservation = reservation;
        initWithReservation();
    }

    public ReservationForm() {}

    private void initWithReservation() {
        Client client = reservation.getClient();
        this.admin = reservation.getAdmin();
        this.statusType = reservation.getStatusType();
        this.firstname = client != null ? client.getFirstName() : null;
        this.lastname = client != null ? client.getLastName() : null;
        this.phone = client != null ? client.getPhone() : null;
        this.email = client != null ? client.getEmail() : null;
        this.countPersons = reservation.getCountPersons();
        this.adminComment = reservation.getAdminComment();
        this.clientComment = reservation.getClientComment();
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCountPersons() {
        return countPersons;
    }

    public void setCountPersons(Integer countPersons) {
        this.countPersons = countPersons;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }
}
