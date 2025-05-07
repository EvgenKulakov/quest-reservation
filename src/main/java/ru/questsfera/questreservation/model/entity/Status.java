package ru.questsfera.questreservation.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.model.dto.StatusType;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Status { // TODO DTO

    private Integer id;
    private StatusType type;
    private String text;

    public Status(StatusType statusType) {
        this.id = statusType.getId();
        this.type = statusType;
        this.text = statusType.getText();
    }

    public Status(String id) {
        this(StatusType.values()[Integer.parseInt(id)]);
    }

    public static Status fromStatusTypeName(String name) {
        return new Status(StatusType.valueOf(name));
    }

    public static List<Status> getUserStatuses() {
        List<Status> userStatuses = new ArrayList<>();
        userStatuses.add(new Status(StatusType.NEW_RESERVE));
        userStatuses.add(new Status(StatusType.CANCEL));
        userStatuses.add(new Status(StatusType.CONFIRMED));
        userStatuses.add(new Status(StatusType.NOT_COME));
        userStatuses.add(new Status(StatusType.COMPLETED));
        return userStatuses;
    }

    public static List<Status> getDefaultStatuses() {
        List<Status> defaultStatuses = new ArrayList<>();
        defaultStatuses.add(new Status(StatusType.NEW_RESERVE));
        defaultStatuses.add(new Status(StatusType.CANCEL));
        return defaultStatuses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status status)) return false;
        return id != null && Objects.equals(getId(), status.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
