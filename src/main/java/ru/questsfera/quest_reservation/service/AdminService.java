package ru.questsfera.quest_reservation.service;

import ru.questsfera.quest_reservation.entity.AdminEntity;

import java.util.List;

public interface AdminService {

    List<AdminEntity> getAllAdminsEntity();

    AdminEntity getAdmin(int id);

    void saveAdmin(AdminEntity adminEntity);

    void deleteAdmin(int id);
}
