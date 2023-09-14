package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.validator.BlockSlotValidator;
import ru.questsfera.questreservation.validator.Patterns;
import ru.questsfera.questreservation.validator.SaveReserveValidator;

public class ReservationForm {

    private static final String ERROR_BLOCK_MESSAGE = "*Для блокировки все поля должны быть пустыми";

    private StatusType statusType;

    @NotBlank(message = "*Обязательное поле", groups = SaveReserveValidator.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = BlockSlotValidator.class)
    private String firstname;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = BlockSlotValidator.class)
    private String lastname;

    @Pattern(regexp = Patterns.PHONE,
            message = "*Введите номер телефона в формате +7хххххххххх",
            groups = SaveReserveValidator.class)
    @Size(max = 2, message = ERROR_BLOCK_MESSAGE, groups = BlockSlotValidator.class)
    private String phone = "+7";

    @Pattern(regexp = Patterns.EMAIL,
            message = "*Проверьте правильное написание Email", groups = SaveReserveValidator.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = BlockSlotValidator.class)
    private String email;

    private Integer countPersons;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = BlockSlotValidator.class)
    private String adminComment;

    private String clientComment;

    public ReservationForm(Reservation reservation) {
        initWithReservation(reservation);
    }

    public ReservationForm() {}

    private void initWithReservation(Reservation reservation) {
        Client client = reservation.getClient();
        this.statusType = reservation.getStatusType();
        this.firstname = client != null ? client.getFirstName() : null;
        this.lastname = client != null ? client.getLastName() : null;
        this.phone = client != null ? client.getPhone() : null;
        this.email = client != null ? client.getEmail() : null;
        this.countPersons = reservation.getCountPersons();
        this.adminComment = reservation.getAdminComment();
        this.clientComment = reservation.getClientComment();
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
