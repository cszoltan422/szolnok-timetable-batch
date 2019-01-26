package org.zenbot.szolnok.timetable.configuration.properties;

import lombok.Data;

import java.util.List;

@Data
public class TimetableResourceProperties {
    private String baseUrl;
    private String szolnokUrl;
    private List<String> selectedBuses;
}
