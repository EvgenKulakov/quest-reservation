package ru.questsfera.questreservation.processor;

import org.springframework.security.crypto.bcrypt.BCrypt;

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

    public static String createBCrypt(String password) {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        return "{bcrypt}" + passwordHash;
    }
}
