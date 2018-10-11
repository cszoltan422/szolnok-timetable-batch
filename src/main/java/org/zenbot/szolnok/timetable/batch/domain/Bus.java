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
public class Bus {

    @Id
    private String id;
    private String busName;
    private List<BusRoute> busRoutes;

    public BusRoute getBusRouteByStartStopName(String startBusStopName) {
        Optional<BusRoute> routeLine = busRoutes.stream()
                .filter(line -> line.getStartBusStop().equals(startBusStopName))
                .findFirst();
        if (routeLine.isPresent()) {
            return routeLine.get();
        } else {
            BusRoute result = new BusRoute();
            result.setBusStops(new ArrayList<>());
            return result;
        }
    }

    public boolean hasNoBusRoute(BusRoute busRoute) {
        return !busRoutes.stream()
                .map(line -> line.getStartBusStop())
                .collect(Collectors.toList())
                .contains(busRoute.getStartBusStop());
    }
}
