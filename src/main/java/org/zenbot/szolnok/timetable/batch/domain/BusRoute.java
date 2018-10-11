package org.zenbot.szolnok.timetable.batch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class BusRoute {

    @Id
    private String id;
    private String startBusStop;
    private String endBusStop;
    private List<BusStop> busStops;

    public void addBusStopTimetable(BusStop busStop) {
        busStops.add(busStop);
    }
}
