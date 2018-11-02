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
import java.util.ArrayList;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableProperties.class)
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TimetableProperties timetableProperties;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, TimetableProperties timetableProperties) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.timetableProperties = timetableProperties;
        if (timetableProperties.getResource().getSelectedBuses() == null) {
            timetableProperties.getResource().setSelectedBuses(new ArrayList<>());
        }
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
        multiResourceItemReader.setDelegate(new HtmlDocumentReaderReader());
        return multiResourceItemReader;
    }

    @Bean
    public ItemProcessor<Document, Timetable> itemProcessor() {
        return new TimetableProcessor(stringCleaner(), timetableProperties.getSelector());
    }

    @Bean
    public ItemWriter<Timetable> itemWriter(BusRepository busRepository, BusStopRepository busStopRepository) {
        return new TimetableItemWriter(busRepository, busStopRepository);
    }

    @Bean
    public JobExecutionListener jobExecutionListener(BusRepository busRepository, BusStopRepository busStopRepository, Environment environment) {
        return new RemoveBusRoutesExecutionListener(busRepository, busStopRepository, environment, timetableProperties.getResource());
    }

    @Bean
    public StringCleaner stringCleaner() {
        return new StringCleaner();
    }
}
