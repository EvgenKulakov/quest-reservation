package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.dto.ReservationForm;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients", schema = "quest_reservations_db")
@JsonIgnoreProperties({"admin"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "blacklist_id")
    private BlackList blackList;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public Client(ReservationForm resForm, Admin admin) {
        this.firstName = resForm.getFirstName();
        this.lastName = resForm.getLastName();
        this.phone = resForm.getPhone();
        this.email = resForm.getEmail();
        this.admin = admin;
    }

    public void deleteBlackListForClient() {
        this.blackList = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return id != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
