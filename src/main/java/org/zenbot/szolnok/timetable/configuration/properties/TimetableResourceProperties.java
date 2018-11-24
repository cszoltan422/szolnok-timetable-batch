package org.zenbot.szolnok.timetable.configuration.properties;

import lombok.Data;

import java.util.List;

@Data
public class TimetableResourceProperties {
    private String folder;
    private String fileExtension;
    private String commentSign;
    private List<String> selectedBuses;
}
