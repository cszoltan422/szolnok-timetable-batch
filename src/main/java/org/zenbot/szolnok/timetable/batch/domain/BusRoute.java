package org.zenbot.szolnok.timetable.batch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Document
public class BusRoute {

    @Id
    private String id;
    private String routename;
    private List<BusRouteLine> busRouteLines;

    public BusRouteLine getRoutePathByStartStopName(String startBusStopName) {
        Optional<BusRouteLine> routeLine = busRouteLines.stream()
                .filter(line -> line.getStartBusStop().equals(startBusStopName))
                .findFirst();
        if (routeLine.isPresent()) {
            return routeLine.get();
        } else {
            BusRouteLine result = new BusRouteLine();
            result.setBusStops(new ArrayList<>());
            return result;
        }
    }

    public boolean hasNoRoutePath(BusRouteLine busRouteLine) {
        return !busRouteLines.stream()
                .map(line -> line.getStartBusStop())
                .collect(Collectors.toList())
                .contains(busRouteLine.getStartBusStop());
    }
}
