package org.zenbot.szolnok.timetable.batch.batch;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Timetable {

    private String routeName;
    private String startBusStopName;
    private String endBusStopName;
    private String activeStopName;
    private Map<Integer, Map<String, String>> timetable = new HashMap<>();

    public void addRow(int hour, Map<String, String> values) {
        timetable.put(hour, values);
    }
}
