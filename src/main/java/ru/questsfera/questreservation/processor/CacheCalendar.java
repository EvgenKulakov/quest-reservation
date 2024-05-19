package ru.questsfera.questreservation.processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CacheCalendar {

    public static Date getDateOfDeletion(LocalDate localDate) {
        LocalDateTime dateTimeOfDeletion = localDate.plusDays(8).atStartOfDay();
        Date dateOfDeletion = Date.from(dateTimeOfDeletion.atZone(ZoneId.systemDefault()).toInstant());
        return dateOfDeletion;
    }

    public static List<LocalDate> getDatesForCache() {
        List<LocalDate> dateList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.plusWeeks(2);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        return dateList;
    }
}
