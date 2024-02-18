package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "blacklist", schema = "quest_reservations_db")
@JsonIgnoreProperties({"admin"})
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "messages")
    private String messages;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

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
