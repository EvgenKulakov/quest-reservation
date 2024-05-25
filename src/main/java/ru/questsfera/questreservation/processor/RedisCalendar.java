package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.StatusType;
import ru.questsfera.questreservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class RedisCalendar {

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

    public static Optional<LocalDate> getLatestDateForRedis(List<Reservation> reservations) {

        Optional<LocalDate> latestDate = reservations
                .stream()
                .filter(res -> res.getStatusType() != StatusType.CANCEL)
                .map(Reservation::getDateReserve)
                .filter(RedisCalendar::isDateForRedis)
                .max(Comparator.naturalOrder());

        return latestDate;
    }

    public static List<LocalDate> getDatesForRedis() {
        List<LocalDate> dateList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.plusWeeks(2);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateList.add(date);
        }

        return dateList;
    }

    public static boolean isDateForRedis(LocalDate checkDate) {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.plusWeeks(2);

        return (checkDate.isEqual(startDate) || checkDate.isAfter(startDate))
                && (checkDate.isEqual(endDate) || checkDate.isBefore(endDate));
    }
}
