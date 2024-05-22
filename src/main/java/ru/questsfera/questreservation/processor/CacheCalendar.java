package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CacheCalendar {

    public static long getTimeToLive(LocalDate localDate) {
        Date dateOfDeletion = getDateOfDeletion(localDate);
        Date now = new Date();
        long timeToLive = dateOfDeletion.getTime() - now.getTime();
        return timeToLive;
    }

    public static Date getDateOfDeletion(LocalDate localDate) {
        LocalDateTime dateTimeOfDeletion = localDate.plusDays(8).atStartOfDay();
        Date dateOfDeletion = Date.from(dateTimeOfDeletion.atZone(ZoneId.systemDefault()).toInstant());
        return dateOfDeletion;
    }

    public static LocalDate getLatestDateReservation(List<Reservation> reservations) {

        LocalDate latestDate = reservations
                .stream()
                .filter(res -> res.getStatusType() != StatusType.CANCEL)
                .map(Reservation::getDateReserve)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        return latestDate;
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
