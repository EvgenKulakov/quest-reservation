package ru.questsfera.questreservation.validator;

public class Patterns {

    public static final String PASSWORD = "^(?!\\s)[\\S]{8,}$";

    public static final String PHONE = "^\\+7\\d{10}$";

    public static final String EMAIL = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}
