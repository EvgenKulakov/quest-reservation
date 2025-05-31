package ru.questsfera.questreservation.validator;

import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.SlotListTypeBuild;
import ru.questsfera.questreservation.model.dto.TimePrice;

import java.util.Collections;
import java.util.List;

public class SlotListValidator {

    private static boolean hasNull;
    private static boolean hasDuplicate;
    private final static String errorHasNull = "*Все поля должны быть заполнены";
    private final static String errorHasDuplicate = "*Все слоты должны быть на разное время";

    public static String checkByType(SlotList slotList, SlotListTypeBuild typeBuild) {
        hasNull = false;
        hasDuplicate = false;

        return switch (typeBuild) {
            case EQUAL_DAYS -> checkEqualDays(slotList);
            case WEEKDAYS_WEEKENDS -> checkWeekdaysWeekends(slotList);
            default -> checkDifferentDays(slotList);
        };
    }

    public static String checkEqualDays(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        return switchErrorText();
    }

    public static String checkWeekdaysWeekends(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        checkOneDay(slotList.getSaturday());
        return switchErrorText();
    }

    public static String checkDifferentDays(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        checkOneDay(slotList.getTuesday());
        checkOneDay(slotList.getWednesday());
        checkOneDay(slotList.getThursday());
        checkOneDay(slotList.getFriday());
        checkOneDay(slotList.getSaturday());
        checkOneDay(slotList.getSunday());
        return switchErrorText();
    }

    public static void checkOneDay(List<TimePrice> oneDay) {
        boolean thisHasNull = hasNull(oneDay);
        if (!thisHasNull) hasDuplicate(oneDay);
    }

    private static boolean hasNull(List<TimePrice> oneDay) {
        for (TimePrice timePrice : oneDay) {
            if (timePrice.getTime() == null || timePrice.getPrice() == null) {
                hasNull = true;
                return true;
            }
        }
        return false;
    }

    private static void hasDuplicate(List<TimePrice> oneDay) {
        Collections.sort(oneDay);
        for (int i = 1; i < oneDay.size(); i++) {
            if (oneDay.get(i).equals(oneDay.get(i - 1))) {
                hasDuplicate = true;
                return;
            }
        }
    }

    private static String switchErrorText() {
        if (hasNull) return errorHasNull;
        if (hasDuplicate) return errorHasDuplicate;
        return "";
    }
}
