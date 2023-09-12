package ru.questsfera.questreservation.entity;

public interface Account {

    void setPasswordHash(String passwordHash);

    String getPasswordHash();
}
