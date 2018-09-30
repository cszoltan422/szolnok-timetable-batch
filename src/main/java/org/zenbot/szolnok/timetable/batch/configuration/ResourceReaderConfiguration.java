package org.zenbot.szolnok.timetable.batch.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(TimetableResourceLocationProperties.class)
public class ResourceReaderConfiguration {

    private final Environment environment;
    private final TimetableResourceLocationProperties properties;

    public ResourceReaderConfiguration(Environment environment, TimetableResourceLocationProperties properties) {
        this.environment = environment;
        this.properties = properties;
    }

    @Bean
    public ResourceReader resourceReader() {
        return new ResourceReader(environment, comparator(), properties);
    }

    @Bean
    public FilenameComparator comparator() {
        return new FilenameComparator();
    }
}
