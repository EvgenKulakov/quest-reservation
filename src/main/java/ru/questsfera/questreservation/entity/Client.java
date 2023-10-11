package ru.questsfera.questreservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import ru.questsfera.questreservation.dto.ReservationForm;
import ru.questsfera.questreservation.dto.Slot;

import java.util.*;

@Entity
@Table(name = "clients", schema = "quest_reservations")
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

    public Client() {}

    public void deleteBlackListForClient() {
        this.blackList = null;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public BlackList getBlackList() {
        return blackList;
    }

    public void setBlackList(BlackList blackList) {
        this.blackList = blackList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
