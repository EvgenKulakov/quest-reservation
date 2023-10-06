package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.questsfera.questreservation.validator.Patterns;
import ru.questsfera.questreservation.validator.Validator;

public class ReservationForm {

    private static final String ERROR_BLOCK_MESSAGE = "*Для блокировки все поля должны быть пустыми";

    private StatusType statusType;

    @NotBlank(message = "*Обязательное поле", groups = Validator.SaveReserve.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = Validator.BlockSlot.class)
    private String firstName;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = Validator.BlockSlot.class)
    private String lastName;

    @Pattern(regexp = Patterns.PHONE,
            message = "*Введите номер телефона в формате +7хххххххххх",
            groups = Validator.SaveReserve.class)
    @Size(max = 2, message = ERROR_BLOCK_MESSAGE, groups = Validator.BlockSlot.class)
    private String phone = "+7";

    @Pattern(regexp = Patterns.EMAIL_OR_EMPTY,
            message = "*Проверьте правильное написание Email", groups = Validator.SaveReserve.class)
    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = Validator.BlockSlot.class)
    private String email;

    private Integer countPersons;

    @Size(max = 0, message = ERROR_BLOCK_MESSAGE, groups = Validator.BlockSlot.class)
    private String adminComment;

    private String clientComment;

    public ReservationForm() {}

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
