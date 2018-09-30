package org.zenbot.szolnok.timetable.batch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BusStop {

    @Id
    private String id;
    private String busStopName;
    private Schedule workDaySchedule;
    private Schedule saturdaySchedule;
    private Schedule sundaySchedule;

}
