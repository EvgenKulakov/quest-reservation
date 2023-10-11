package ru.questsfera.questreservation.dto;

import ru.questsfera.questreservation.entity.Admin;
import ru.questsfera.questreservation.entity.Quest;

import java.util.Set;

public interface Account {

    String getUsername();
    String getPassword();
    Set<Quest> getQuests();
    Role getRole();
    Admin getAdmin();
}
