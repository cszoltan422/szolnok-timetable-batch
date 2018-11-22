package org.zenbot.szolnok.timetable.batch.batch.step.bus.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;
import org.zenbot.szolnok.timetable.batch.domain.Bus;

import java.util.List;

@Slf4j
@Component
public class BusMongoRepositoryItemWriter implements ItemWriter<Bus> {

    private final BusRepository busRepository;

    public BusMongoRepositoryItemWriter(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public void write(List<? extends Bus> list) throws Exception {
        if (list.size()  > 1) {
            throw new IllegalArgumentException("Size of the list should be [1]! Actual size is [" + list.size() + "]");
        }

        Bus bus = list.get(0);
        log.info("Saving bus=[#{}, from={}, to={}] to database", bus.getBusName(), bus.getBusRoutes().get(0).getStartBusStop(), bus.getBusRoutes().get(0).getEndBusStop());
        busRepository.save(bus);
    }
}
