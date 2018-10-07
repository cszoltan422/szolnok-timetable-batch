package org.zenbot.szolnok.timetable.batch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.zenbot.szolnok.timetable.batch.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.batch.dao.RouteRepository;
import org.zenbot.szolnok.timetable.batch.domain.*;

import java.util.*;

@Slf4j
public class TimetableItemWriter implements ItemWriter<Timetable> {

    private final RouteRepository routeRepository;
    private final BusStopRepository busStopRepository;

    public TimetableItemWriter(RouteRepository routeRepository, BusStopRepository busStopRepository) {
        this.routeRepository = routeRepository;
        this.busStopRepository = busStopRepository;
    }

    @Override
    public void write(List<? extends Timetable> list) {
        checkConstraints(list);
        Timetable timetable = buildBusRouteAndSave(list);
        saveBusStop(timetable);
    }

    private void saveBusStop(Timetable timetable) {
        Optional<BusStopWithRoutes> busStopWithRoutesOptional = busStopRepository.findByBusStopName(timetable.getActiveStopName());
        BusStopWithRoutes busStopWithRoutes = null;
        if (!busStopWithRoutesOptional.isPresent()) {
            busStopWithRoutes = new BusStopWithRoutes();
            busStopWithRoutes.setBusStopName(timetable.getActiveStopName());
            busStopWithRoutes.setBusRoutes(new HashSet<>(Collections.singletonList(timetable.getRouteName())));
        } else {
            busStopWithRoutes = busStopWithRoutesOptional.get();
            busStopWithRoutes.getBusRoutes().add(timetable.getRouteName());
        }
        log.info("Saving busStop=[{}] to database", busStopWithRoutes);
        busStopRepository.save(busStopWithRoutes);
    }

    private Timetable buildBusRouteAndSave(List<? extends Timetable> list) {
        Timetable timetable = list.get(0);
        log.info("Writing busRoute [#{} from={}, stop={}] to database", timetable.getRouteName(), timetable.getStartBusStopName(), timetable.getActiveStopName());
        BusRoute busRoute = getRoute(timetable);
        BusRouteLine busRouteLine = getRoutePath(timetable, busRoute);
        BusStop busStop = new BusStop();


        buildBusStopTimeTable(timetable, busStop);
        setRoutePathProperties(busRouteLine, timetable, busStop);
        addRoutePath(busRoute, busRouteLine);

        log.info("Saving to database [#{}]", busRoute.getRoutename());
        routeRepository.save(busRoute);
        return timetable;
    }

    private void addRoutePath(BusRoute busRoute, BusRouteLine busRouteLine) {
        if (busRoute.hasNoRoutePath(busRouteLine)) {
            busRoute.getBusRouteLines().add(busRouteLine);
        }
    }

    private void setRoutePathProperties(BusRouteLine busRouteLine, Timetable timetable, BusStop busStop) {
        busRouteLine.addBusStopTimetable(busStop);
        busRouteLine.setStartBusStop(timetable.getStartBusStopName());
        busRouteLine.setEndBusStop(timetable.getEndBusStopName());
    }

    private void buildBusStopTimeTable(Timetable timetable, BusStop busStop) {
        log.debug("Setting bust stop properties");
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

    private BusRouteLine getRoutePath(Timetable timetable, BusRoute busRoute) {
        log.debug("Fetching routepath from busRoute [#{}]", busRoute.getRoutename());
        return busRoute.getRoutePathByStartStopName(timetable.getStartBusStopName());
    }

    private BusRoute getRoute(Timetable timetable) {
        log.debug("Fetching busRoute from database");
        BusRoute busRoute = routeRepository.findByRoutename(timetable.getRouteName());
        if (busRoute == null) {
            log.debug("BusRoute not found! Creating a new one!");
            busRoute = new BusRoute();
            busRoute.setBusRouteLines(new ArrayList<>());
        }
        busRoute.setRoutename(timetable.getRouteName());
        return busRoute;
    }

    private void checkConstraints(List<? extends Timetable> list) {
        if (list.size() != 1) {
            throw new IllegalArgumentException("The size of the list to write should be one! Writing items one by one is accepted");
        }
    }
}
