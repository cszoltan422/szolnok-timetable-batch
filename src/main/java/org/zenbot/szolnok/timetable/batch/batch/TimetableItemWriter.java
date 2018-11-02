package org.zenbot.szolnok.timetable.batch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.zenbot.szolnok.timetable.batch.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;
import org.zenbot.szolnok.timetable.batch.domain.*;

import java.util.*;

@Slf4j
public class TimetableItemWriter implements ItemWriter<Timetable> {

    private final BusRepository busRepository;
    private final BusStopRepository busStopRepository;

    public TimetableItemWriter(BusRepository busRepository, BusStopRepository busStopRepository) {
        this.busRepository = busRepository;
        this.busStopRepository = busStopRepository;
    }

    @Override
    public void write(List<? extends Timetable> list) {
        checkConstraints(list);
        Bus bus = buildBus(list.get(0));
        busRepository.save(bus);
        saveBusStop(list.get(0));
    }

    private void saveBusStop(Timetable timetable) {
        Optional<BusStopWithBuses> busStopWithRoutesOptional = busStopRepository.findByBusStopName(timetable.getActiveStopName());
        BusStopWithBuses busStopWithBuses;
        if (!busStopWithRoutesOptional.isPresent()) {
            busStopWithBuses = new BusStopWithBuses();
            busStopWithBuses.setBusStopName(timetable.getActiveStopName());
            busStopWithBuses.setBuses(new HashSet<>(Collections.singletonList(timetable.getBusName())));
        } else {
            busStopWithBuses = busStopWithRoutesOptional.get();
            busStopWithBuses.getBuses().add(timetable.getBusName());
        }
        log.info("Saving busStop=[{}] to database", busStopWithBuses);
        busStopRepository.save(busStopWithBuses);
    }

    private Bus buildBus(Timetable timetable) {
        log.info("Writing bus [#{} from={}, stop={}, to={}] to database", timetable.getBusName(), timetable.getStartBusStopName(), timetable.getActiveStopName(), timetable.getEndBusStopName());
        Bus bus = getBus(timetable);
        BusRoute busRoute = getBusRoute(timetable, bus);
        BusStop busStop = new BusStop();

        buildBusStopTimeTable(timetable, busStop);
        setRoutePathProperties(busRoute, timetable, busStop);
        addRoutePath(bus, busRoute);

        log.info("Saving to database bus=[#{}]", bus.getBusName());
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

        setScheduleValues(timetable, workdaySchedule, TimetableProcessor.WEEKDAY_KEY);
        setScheduleValues(timetable, saturdaySchedule, TimetableProcessor.SATURDAY_KEY);
        setScheduleValues(timetable, sundaySchedule, TimetableProcessor.SUNDAY_KEY);

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

    private void checkConstraints(List<? extends Timetable> list) {
        if (list.size() != 1) {
            throw new IllegalArgumentException("The size of the list to write should be one! Writing items one by one is accepted");
        }
    }
}
