package org.zenbot.szolnok.timetable.batch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableResourceProperties;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;
import org.zenbot.szolnok.timetable.batch.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.batch.domain.Bus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RemoveBusRoutesExecutionListener implements JobExecutionListener {

    private final BusRepository busRepository;
    private final BusStopRepository busStopRepository;
    private final TimetableResourceProperties properties;

    public RemoveBusRoutesExecutionListener(BusRepository busRepository, BusStopRepository busStopRepository, TimetableResourceProperties properties) {
        this.busRepository = busRepository;
        this.busStopRepository = busStopRepository;
        this.properties = properties;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        List<Bus> routes = busRepository.findAll();
        if (!properties.getSelectedBuses().isEmpty()) {
            log.info("Removing bus routes [{}]", String.join(",", properties.getSelectedBuses()));
            routes.forEach(route -> {
                if (properties.getSelectedBuses().contains(route.getBusName())) {
                    route.setBusRoutes(new ArrayList<>());
                }
            });
        } else {
            log.info("Removing all bus routes from database");
            routes.forEach(route -> route.setBusRoutes(new ArrayList<>()));
            busStopRepository.deleteAll();
        }
        busRepository.saveAll(routes);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
    }
}
