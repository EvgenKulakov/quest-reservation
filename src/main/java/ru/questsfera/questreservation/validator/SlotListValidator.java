package ru.questsfera.questreservation.validator;

import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.SlotList;
import ru.questsfera.questreservation.model.dto.SlotListTypeBuild;
import ru.questsfera.questreservation.model.dto.TimePrice;

import java.util.Collections;
import java.util.List;

@Component
public class SlotListValidator {

    private static boolean hasNull;
    private static boolean hasDuplicate;
    private final static String errorHasNull = "*Все поля должны быть заполнены";
    private final static String errorHasDuplicate = "*Все слоты должны быть на разное время";

    public String checkByType(SlotList slotList, SlotListTypeBuild typeBuild) {
        hasNull = false;
        hasDuplicate = false;

        return switch (typeBuild) {
            case EQUAL_DAYS -> checkEqualDays(slotList);
            case WEEKDAYS_WEEKENDS -> checkWeekdaysWeekends(slotList);
            default -> checkDifferentDays(slotList);
        };
    }

    private String checkEqualDays(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        return switchErrorText();
    }

    private String checkWeekdaysWeekends(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        checkOneDay(slotList.getSaturday());
        return switchErrorText();
    }

    private String checkDifferentDays(SlotList slotList) {
        checkOneDay(slotList.getMonday());
        checkOneDay(slotList.getTuesday());
        checkOneDay(slotList.getWednesday());
        checkOneDay(slotList.getThursday());
        checkOneDay(slotList.getFriday());
        checkOneDay(slotList.getSaturday());
        checkOneDay(slotList.getSunday());
        return switchErrorText();
    }

    private void checkOneDay(List<TimePrice> oneDay) {
        boolean thisHasNull = hasNull(oneDay);
        if (!thisHasNull) hasDuplicate(oneDay);
    }

    private boolean hasNull(List<TimePrice> oneDay) {
        for (TimePrice timePrice : oneDay) {
            if (timePrice.getTime() == null || timePrice.getPrice() == null) {
                hasNull = true;
                return true;
            }
        }
        return false;
    }

    private void hasDuplicate(List<TimePrice> oneDay) {
        Collections.sort(oneDay);
        for (int i = 1; i < oneDay.size(); i++) {
            if (oneDay.get(i).equals(oneDay.get(i - 1))) {
                hasDuplicate = true;
                return;
            }
        }
    }

    private String switchErrorText() {
        if (hasNull) return errorHasNull;
        if (hasDuplicate) return errorHasDuplicate;
        return "";
    }
}
