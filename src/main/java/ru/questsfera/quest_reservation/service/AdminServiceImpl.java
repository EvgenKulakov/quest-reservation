package ru.questsfera.quest_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.questsfera.quest_reservation.dao.AdminRepository;
import ru.questsfera.quest_reservation.entity.AdminEntity;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<AdminEntity> getAllAdminsEntity() {
        return adminRepository.findAll();
    }

    @Override
    public AdminEntity getAdmin(int id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public void saveAdmin(AdminEntity adminEntity) {
        adminRepository.save(adminEntity);
    }

    @Override
    public void deleteAdmin(int id) {
        adminRepository.deleteById(id);
    }
}
