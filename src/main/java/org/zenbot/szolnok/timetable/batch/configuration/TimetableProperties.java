package org.zenbot.szolnok.timetable.batch.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableResourceProperties;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableSelectorProperties;

@Data
@ConfigurationProperties("timetable")
public class TimetableProperties {
    private TimetableResourceProperties resource;
    private TimetableSelectorProperties selector;
}
