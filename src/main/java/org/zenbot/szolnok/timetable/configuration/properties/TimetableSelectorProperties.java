package org.zenbot.szolnok.timetable.configuration.properties;

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
    private String routesLinkSelector;
    private String hrefSelector;
    private String stationsSelector;
    private String otherRouteSelector;
}
