package org.zenbot.timetableupdater.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(TimetableResourceLocationProperties.class)
public class ResourceReaderConfiguration {

    private final Environment environment;
    private final TimetableResourceLocationProperties properties;
    private final FilenameComparator comparator;

    public ResourceReaderConfiguration(Environment environment, TimetableResourceLocationProperties properties, FilenameComparator comparator) {
        this.environment = environment;
        this.properties = properties;
        this.comparator = comparator;
    }

    @Bean
    public ResourceReader resourceReader() {
        return new ResourceReader(environment, comparator, properties);
    }
}
