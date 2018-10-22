package org.zenbot.szolnok.timetable.batch.configuration.properties;

import lombok.Data;

@Data
public class TimetableSelectorProperties {
    private String routeNameSelector;
    private String fromSelector;
    private String actualStopSelector;
    private String busStopsSelector;
    private String timetableSelector;
    private String tableRowSelector;
    private String tableColumnSelector;
}
