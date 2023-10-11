package ru.questsfera.questreservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.repository.*;
import ru.questsfera.questreservation.entity.*;

import java.util.*;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Admin getAdminByName(String username) {
        Optional<Admin> adminOptional = adminRepository.findAdminByUsername(username);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }
        throw new UsernameNotFoundException(String.format("Пользователь %s не найден", username));
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }
}
