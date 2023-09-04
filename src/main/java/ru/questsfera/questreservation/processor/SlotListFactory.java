package ru.questsfera.questreservation.processor;

import ru.questsfera.questreservation.dto.SlotList;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SlotListFactory {

    public static SlotList create(List<LocalTime> timesWeekday, List<Integer> pricesWeekday,
                                  List<LocalTime> timesWeekend, List<Integer> pricesWeekend) {

        LinkedHashMap<LocalTime, Integer> weekdays = createOneDay(timesWeekday, pricesWeekday);
        LinkedHashMap<LocalTime, Integer> weekends = createOneDay(timesWeekend, pricesWeekend);

        return new SlotList(weekdays, weekends);
    }

    private static LinkedHashMap<LocalTime, Integer> createOneDay(List<LocalTime> times, List<Integer> prices) {
        return IntStream
                .range(0, times.size())
                .boxed()
                .collect(Collectors.toMap(times::get, prices::get, (v1, v2) -> v1, LinkedHashMap::new));
    }
}
