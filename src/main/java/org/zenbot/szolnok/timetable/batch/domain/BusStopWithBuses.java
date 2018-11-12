package org.zenbot.szolnok.timetable.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class BusStopWithBuses {

    @Id
    private String id;

    private String busStopName;
    private Set<String> buses;
}
