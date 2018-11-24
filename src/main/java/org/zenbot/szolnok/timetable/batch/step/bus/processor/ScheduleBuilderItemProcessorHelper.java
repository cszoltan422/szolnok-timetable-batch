package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.domain.BusArrival;
import org.zenbot.szolnok.timetable.domain.Schedule;
import org.zenbot.szolnok.timetable.domain.Timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ScheduleBuilderItemProcessorHelper {
    public Schedule buildSchedule(Timetable timetable, String weekdayKey) {
        log.debug("Building schedule for day: [{}]", weekdayKey);
        List<BusArrival> busArrivalsByHour = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> entries : timetable.getTimetable().entrySet()) {
            Integer key = entries.getKey();
            Map<String, String> value = entries.getValue();
            String arrivals = value.get(weekdayKey);
            String[] arrivalsSplitted = arrivals.split(",");
            List<BusArrival> busArrivals = new ArrayList<>();
            for (String arrival : arrivalsSplitted) {
                BusArrival busArrival = new BusArrival();
                busArrival.setArrivalHour(key);
                busArrival.setArrivalMinute(arrival.isEmpty() ? null : Integer.valueOf(arrival));
                busArrivals.add(busArrival);
            }
            busArrivalsByHour.addAll(busArrivals);
        }
        Schedule schedule = new Schedule();
        schedule.setBusArrivals(busArrivalsByHour);
        return schedule;
    }
}
