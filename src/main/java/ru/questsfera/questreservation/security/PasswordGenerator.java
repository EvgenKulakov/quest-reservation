package ru.questsfera.questreservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PasswordGenerator {

    private final PasswordEncoder passwordEncoder;

    public String createRandomPassword() {
        int passwordSize = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(passwordSize);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < passwordSize; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            password.append(randomChar);
        }

        return password.toString();
    }

    public String createPasswordHash(String password) {
        String passwordHash = passwordEncoder.encode(password);
        return passwordHash;
    }
}
