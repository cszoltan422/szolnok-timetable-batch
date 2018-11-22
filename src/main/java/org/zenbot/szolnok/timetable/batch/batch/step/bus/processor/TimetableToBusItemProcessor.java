package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;
import org.zenbot.szolnok.timetable.batch.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TimetableToBusItemProcessor implements ItemProcessor<Timetable, Bus> {

    private final BusRepository busRepository;

    public TimetableToBusItemProcessor(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public Bus process(Timetable timetable) throws Exception {
        return buildBus(timetable);
    }

    private Bus buildBus(Timetable timetable) {
        log.info("Processing timetable [#{} from={}, stop={}, to={}]", timetable.getBusName(), timetable.getStartBusStopName(), timetable.getActiveStopName(), timetable.getEndBusStopName());
        Bus bus = getBus(timetable);
        BusRoute busRoute = getBusRoute(timetable, bus);
        BusStop busStop = new BusStop();

        buildBusStopTimeTable(timetable, busStop);
        setRoutePathProperties(busRoute, timetable, busStop);
        addRoutePath(bus, busRoute);

        log.info("Done processing timetable to bus=[#{}]", bus.getBusName());
        return bus;
    }

    private void addRoutePath(Bus bus, BusRoute busRoute) {
        if (bus.hasNoBusRoute(busRoute)) {
            bus.getBusRoutes().add(busRoute);
        }
    }

    private void setRoutePathProperties(BusRoute busRoute, Timetable timetable, BusStop busStop) {
        busRoute.addBusStopTimetable(busStop);
        busRoute.setStartBusStop(timetable.getStartBusStopName());
        busRoute.setEndBusStop(timetable.getEndBusStopName());
    }

    private void buildBusStopTimeTable(Timetable timetable, BusStop busStop) {
        log.debug("Setting bus stop properties for stop=[{}] and bus=[{}]", timetable.getActiveStopName(), timetable.getBusName());
        busStop.setBusStopName(timetable.getActiveStopName());
        Schedule workdaySchedule = new Schedule();
        Schedule saturdaySchedule = new Schedule();
        Schedule sundaySchedule = new Schedule();

        setScheduleValues(timetable, workdaySchedule, JsoupDocumentToTimetableProcessor.WEEKDAY_KEY);
        setScheduleValues(timetable, saturdaySchedule, JsoupDocumentToTimetableProcessor.SATURDAY_KEY);
        setScheduleValues(timetable, sundaySchedule, JsoupDocumentToTimetableProcessor.SUNDAY_KEY);

        busStop.setWorkDaySchedule(workdaySchedule);
        busStop.setSaturdaySchedule(saturdaySchedule);
        busStop.setSundaySchedule(sundaySchedule);
    }

    private void setScheduleValues(Timetable timetable, Schedule workdaySchedule, String day) {
        log.debug("Building schedule for day: [{}]", day);
        List<BusArrival> busArrivalsByHour = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> entries : timetable.getTimetable().entrySet()) {
            Integer key = entries.getKey();
            Map<String, String> value = entries.getValue();
            String arrivals = value.get(day);
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
        workdaySchedule.setBusArrivals(busArrivalsByHour);

    }

    private BusRoute getBusRoute(Timetable timetable, Bus bus) {
        log.debug("Fetching bus route from bus=[#{}]", bus.getBusName());
        return bus.getBusRouteByStartStopName(timetable.getStartBusStopName());
    }

    private Bus getBus(Timetable timetable) {
        log.debug("Fetching bus=[#{}] from database", timetable.getBusName());
        Bus bus = busRepository.findByBusName(timetable.getBusName());
        if (bus == null) {
            log.debug("Bus=[{}] not found! Creating a new one!", timetable.getBusName());
            bus = new Bus();
            bus.setBusRoutes(new ArrayList<>());
        }
        bus.setBusName(timetable.getBusName());
        return bus;
    }
}
