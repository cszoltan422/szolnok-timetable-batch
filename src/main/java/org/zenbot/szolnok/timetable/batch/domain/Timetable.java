package org.zenbot.szolnok.timetable.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {

    private String busName;
    private String startBusStopName;
    private String endBusStopName;
    private String activeStopName;
    private Map<Integer, Map<String, String>> timetable = new HashMap<>();

    public void addRow(int hour, Map<String, String> values) {
        timetable.put(hour, values);
    }
}
