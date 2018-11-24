package org.zenbot.szolnok.timetable.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("timetable")
public class TimetableProperties {
    private TimetableResourceProperties resource;
    private TimetableSelectorProperties selector;
}
