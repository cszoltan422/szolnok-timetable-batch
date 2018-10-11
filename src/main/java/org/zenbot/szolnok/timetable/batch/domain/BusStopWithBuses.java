package org.zenbot.szolnok.timetable.batch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
public class BusStopWithBuses {

    @Id
    private String id;

    private String busStopName;
    private Set<String> buses;
}
