package org.zenbot.szolnok.timetable.batch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.core.env.Environment;
import org.zenbot.szolnok.timetable.batch.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.batch.domain.Bus;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class RemoveBusRoutesExecutionListener implements JobExecutionListener {

    private final BusRepository busRepository;
    private final BusStopRepository busStopRepository;
    private final Environment environment;

    public RemoveBusRoutesExecutionListener(BusRepository busRepository, BusStopRepository busStopRepository, Environment environment) {
        this.busRepository = busRepository;
        this.busStopRepository = busStopRepository;
        this.environment = environment;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        List<String> activeProfilesList = Arrays.asList(environment.getActiveProfiles());
        List<Bus> routes = busRepository.findAll();
        if (!activeProfilesList.isEmpty()) {
            log.info("Removing bus routes [{}]", String.join(",", activeProfilesList));
            routes.forEach(route -> {
                if (activeProfilesList.contains(route.getBusName())) {
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
