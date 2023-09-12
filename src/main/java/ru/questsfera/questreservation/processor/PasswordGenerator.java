package ru.questsfera.questreservation.processor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.questsfera.questreservation.entity.Account;
import ru.questsfera.questreservation.entity.User;

import java.security.SecureRandom;

public class PasswordGenerator {

    public static String createRandomPassword() {
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

    public static void createBCrypt(Account account) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = account.getPasswordHash();
        String passwordHash = encoder.encode(password);
        account.setPasswordHash(passwordHash);
    }
}
