package org.zenbot.szolnok.timetable.batch.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zenbot.szolnok.timetable.batch.batch.step.readurls.ReadFileResourcesTasklet;

@Configuration
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
