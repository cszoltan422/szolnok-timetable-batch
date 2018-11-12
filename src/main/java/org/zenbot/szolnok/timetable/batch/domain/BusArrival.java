package org.zenbot.szolnok.timetable.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class BusArrival {

    @Id
    private String id;
    private boolean lowfloor;
    private int arrivalHour;
    // Might be null as there could be no arrival in a particular hour
    private Integer arrivalMinute;
}
