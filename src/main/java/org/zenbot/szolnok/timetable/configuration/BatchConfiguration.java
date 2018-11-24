package org.zenbot.szolnok.timetable.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zenbot.szolnok.timetable.batch.listener.RemoveBusRoutesExecutionListener;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties;
import org.zenbot.szolnok.timetable.dao.BusRepository;
import org.zenbot.szolnok.timetable.dao.BusStopRepository;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableProperties.class)
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final TimetableProperties timetableProperties;
    private final Step readUrlsStep;
    private final Step saveBusStep;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, TimetableProperties timetableProperties, Step readUrlsStep, Step saveBusStep) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.timetableProperties = timetableProperties;
        this.readUrlsStep = readUrlsStep;
        this.saveBusStep = saveBusStep;
    }

    @Bean
    public Job importTimetableJob(BusRepository busRepository, BusStopRepository busStopRepository) {
        return jobBuilderFactory
                .get("importTimetableJob")
                .listener(jobExecutionListener(busRepository, busStopRepository))
                .start(readUrlsStep)
                .next(saveBusStep)
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener(BusRepository busRepository, BusStopRepository busStopRepository) {
        return new RemoveBusRoutesExecutionListener(busRepository, busStopRepository, timetableProperties.getResource());
    }
}
