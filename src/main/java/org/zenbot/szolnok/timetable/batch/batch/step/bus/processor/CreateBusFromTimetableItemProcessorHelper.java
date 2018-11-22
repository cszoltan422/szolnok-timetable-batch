package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;
import org.zenbot.szolnok.timetable.batch.domain.Bus;
import org.zenbot.szolnok.timetable.batch.domain.Timetable;

import java.util.ArrayList;

@Slf4j
@Component
public class CreateBusFromTimetableItemProcessorHelper {
    
    private final BusRepository busRepository;

    public CreateBusFromTimetableItemProcessorHelper(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public Bus createBusFromTimetable(Timetable timetable) {
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
