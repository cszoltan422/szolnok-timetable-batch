package org.zenbot.szolnok.timetable.batch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BusArrival {

    @Id
    private String id;
    private boolean lowfloor;
    private int arrivalHour;
    // Might be null as there could be no arrival in a particular hour
    private Integer arrivalMinute;
}
