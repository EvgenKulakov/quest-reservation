package ru.questsfera.questreservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.questsfera.questreservation.dto.ResFormDTO;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(name = "clients_seq", sequenceName = "clients_id_seq", allocationSize = 1)
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

    @Column(name = "blacklist_id")
    private Integer blackListId;

    @Column(name = "company_id")
    private Integer companyId;

    public Client(ResFormDTO resForm, Integer companyId) {
        this.firstName = resForm.getFirstName();
        this.lastName = resForm.getLastName();
        this.phones = resForm.getPhone();
        this.emails = resForm.getEmail();
        this.companyId = companyId;
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
