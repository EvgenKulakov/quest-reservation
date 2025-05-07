package ru.questsfera.questreservation.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blacklist")
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blacklist_seq")
    @SequenceGenerator(name = "blacklist_seq", sequenceName = "blacklist_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "messages")
    private String messages;

    @Column(name = "company_id")
    private Integer companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackList blackList)) return false;
        return id != null && Objects.equals(getId(), blackList.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
