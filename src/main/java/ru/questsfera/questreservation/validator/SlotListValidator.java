package ru.questsfera.questreservation.validator;

import ru.questsfera.questreservation.dto.TimePrice;

import java.util.Collections;
import java.util.List;

public class SlotListValidator {

    public static String checkOneDay(List<TimePrice> oneDay) {
        String errorMessage = hasNull(oneDay);
        return errorMessage.isEmpty() ? hasDuplicate(oneDay) : errorMessage;
    }

    private static String hasNull(List<TimePrice> oneDay) {
        String errorMessage = "";
        for (TimePrice timePrice : oneDay) {
            if (timePrice.getTime() == null || timePrice.getPrice() == null) {
                errorMessage = "*Все поля должны быть заполнены";
                return errorMessage;
            }
        }
        return errorMessage;
    }

    private static String hasDuplicate(List<TimePrice> oneDay) {
        String errorMessage = "";
        Collections.sort(oneDay);
        for (int i = 1; i < oneDay.size(); i++) {
            if (oneDay.get(i).equals(oneDay.get(i - 1))) {
                errorMessage = "*Все слоты должны быть на разное время";
                return errorMessage;
            }
        }
        return errorMessage;
    }
}
