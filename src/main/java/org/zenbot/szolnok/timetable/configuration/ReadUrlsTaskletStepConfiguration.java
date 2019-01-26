package org.zenbot.szolnok.timetable.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zenbot.szolnok.timetable.batch.step.readurls.ReadFileResourcesTasklet;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties;

@Configuration
@EnableConfigurationProperties(TimetableProperties.class)
public class ReadUrlsTaskletStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final ReadFileResourcesTasklet readFileResourcesTasklet;

    public ReadUrlsTaskletStepConfiguration(StepBuilderFactory stepBuilderFactory, ReadFileResourcesTasklet readFileResourcesTasklet) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.readFileResourcesTasklet = readFileResourcesTasklet;
    }

    @Bean
    public Step readUrlsStep() {
        return stepBuilderFactory.get("readUrlsStep")
                .tasklet(readFileResourcesTasklet)
                .build();
    }
}
