package ru.questsfera.quest_reservation.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients")
public class ClientsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Basic(optional = false)
    @Column(name = "mail")
    private String mail;

    @OneToOne
    @JoinColumn(name = "black_list_id")
    private BlackListEntity blackList;

    @OneToMany(mappedBy = "client")
    private Set<ReservationsEntity> reservations = new HashSet<>();

    public ClientsEntity() {}

    public Set<ReservationsEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationsEntity> reservations) {
        this.reservations = reservations;
    }

    public BlackListEntity getBlackList() {
        return blackList;
    }

    public void setBlackList(BlackListEntity blackList) {
        this.blackList = blackList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsEntity that = (ClientsEntity) o;
        return id == that.id
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(phone, that.phone)
                && Objects.equals(mail, that.mail)
                && Objects.equals(blackList, that.blackList)
                && Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, mail, blackList, reservations);
    }
}
