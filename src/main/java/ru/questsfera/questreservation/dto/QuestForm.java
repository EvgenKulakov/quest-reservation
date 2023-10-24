package ru.questsfera.questreservation.dto;

import jakarta.validation.constraints.*;
import ru.questsfera.questreservation.entity.Status;
import ru.questsfera.questreservation.entity.User;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class QuestForm {

    @NotBlank(message = "*Обязательное поле")
    private String questName;

    @Min(value = 1, message = "*Минимум 1")
    @Max(value = 100, message = "*Максимум 100")
    @NotNull(message = "*Обязательное поле")
    private Integer minPersons;

    @Min(value = 1, message = "*Минимум 1")
    @Max(value = 100, message = "*Максимум 100")
    @NotNull(message = "*Обязательное поле")
    private Integer maxPersons;

    private Boolean errorCountPersons;

    private Boolean onlySecondPageError;

    private Set<Status> statuses = Status.getDefaultStatuses();

    @NotNull(message = "*Обязательное поле")
    private LocalTime autoBlock = LocalTime.MIN;

    private Set<User> users = new HashSet<>();

    private SlotList slotList = new SlotList();

    private SlotListTypeBuilder typeBuilder = SlotListTypeBuilder.EQUAL_DAYS;

    public QuestForm() {}

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName.trim();
    }

    public Integer getMinPersons() {
        return minPersons;
    }

    public void setMinPersons(Integer minPersons) {
        this.minPersons = minPersons;
    }

    public Integer getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
    }

    public Boolean getErrorCountPersons() {
        return errorCountPersons;
    }

    public void setErrorCountPersons(Boolean errorCountPersons) {
        this.errorCountPersons = errorCountPersons;
    }

    public Boolean getOnlySecondPageError() {
        return onlySecondPageError;
    }

    public void setOnlySecondPageError(Boolean onlySecondPageError) {
        this.onlySecondPageError = onlySecondPageError;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

    public LocalTime getAutoBlock() {
        return autoBlock;
    }

    public void setAutoBlock(LocalTime autoBlock) {
        this.autoBlock = autoBlock;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public SlotList getSlotList() {
        return slotList;
    }

    public void setSlotList(SlotList slotList) {
        this.slotList = slotList;
    }

    public SlotListTypeBuilder getTypeBuilder() {
        return typeBuilder;
    }

    public void setTypeBuilder(SlotListTypeBuilder typeBuilder) {
        this.typeBuilder = typeBuilder;
    }
}
