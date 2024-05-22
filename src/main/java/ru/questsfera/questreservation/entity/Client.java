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
@JsonIgnoreProperties({"company", "reservations"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    //TODO: phones and emails: новая таблица: связь с клиентами и бронированиями
    @Column(name = "phone")
    private String phones;

    @Column(name = "email")
    private String emails;

    @Column(name = "comments")
    private String comments;

    @OneToOne
    @JoinColumn(name = "blacklist_id")
    private BlackList blackList;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservations = new ArrayList<>();

    public Client(ReservationForm resForm, Company company) {
        this.firstName = resForm.getFirstName();
        this.lastName = resForm.getLastName();
        this.phones = resForm.getPhone();
        this.emails = resForm.getEmail();
        this.company = company;
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
