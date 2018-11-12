package org.zenbot.szolnok.timetable.batch.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TimetableProperties.class)
public class ResourceReaderConfiguration {

    private final TimetableProperties properties;

    public ResourceReaderConfiguration(TimetableProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ResourceReader resourceReader() {
        return new ResourceReader(comparator(), properties.getResource());
    }

    @Bean
    public FilenameComparator comparator() {
        return new FilenameComparator();
    }
}
