package org.zenbot.szolnok.timetable.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.zenbot.szolnok.timetable.batch.batch.*;
import org.zenbot.szolnok.timetable.batch.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.batch.dao.BusRepository;

import java.io.IOException;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableResourceLocationProperties.class)
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job importTimetableJob(BusRepository busRepository, BusStopRepository busStopRepository, Environment environment, ResourceReader resourceReader) throws IOException {
        return jobBuilderFactory
                .get("importTimetableJob")
                .listener(jobExecutionListener(busRepository, busStopRepository,environment))
                .flow(importTimetableStep(resourceReader, busRepository, busStopRepository))
                .end()
                .build();
    }

    @Bean
    public Step importTimetableStep(ResourceReader resourceReader, BusRepository busRepository, BusStopRepository busStopRepository) throws IOException {
        return stepBuilderFactory
                .get("importTimetableStep")
                .<Document, Timetable> chunk(1)
                .reader(timetableReader(resourceReader))
                .processor(itemProcessor())
                .writer(itemWriter(busRepository, busStopRepository))
                .build();
    }

    @Bean
    public ItemReader<Document> timetableReader(ResourceReader resourceReader) throws IOException {
        Resource[] resources = resourceReader.readResources().stream().toArray(Resource[]::new);
        MultiResourceItemReader<Document> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.setDelegate(new TimetableReader());
        return multiResourceItemReader;
    }

    @Bean
    public ItemProcessor<Document, Timetable> itemProcessor() {
        return new TimetableProcessor(stringCleaner());
    }

    @Bean
    public ItemWriter<Timetable> itemWriter(BusRepository busRepository, BusStopRepository busStopRepository) {
        return new TimetableItemWriter(busRepository, busStopRepository);
    }

    @Bean
    public JobExecutionListener jobExecutionListener(BusRepository busRepository, BusStopRepository busStopRepository, Environment environment) {
        return new RemoveBusRoutesExecutionListener(busRepository, busStopRepository, environment);
    }

    @Bean
    public StringCleaner stringCleaner() {
        return new StringCleaner();
    }
}