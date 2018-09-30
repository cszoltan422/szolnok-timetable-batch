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
import org.zenbot.szolnok.timetable.batch.dao.RouteRepository;

import java.io.IOException;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(TimetableResourceLocationProperties.class)
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ResourceReader resourceReader;
    private final RouteRepository routeRepository;
    private final Environment environment;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ResourceReader resourceReader, RouteRepository routeRepository, Environment environment) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.resourceReader = resourceReader;
        this.routeRepository = routeRepository;
        this.environment = environment;
    }

    @Bean
    public Job importTimetableJob() throws IOException {
        return jobBuilderFactory
                .get("importTimetableJob")
                .listener(jobExecutionListener())
                .flow(importTimetableStep())
                .end()
                .build();
    }

    @Bean
    public Step importTimetableStep() throws IOException {
        return stepBuilderFactory
                .get("importTimetableStep")
                .<Document, Timetable> chunk(1)
                .reader(timetableReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<Document> timetableReader() throws IOException {
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
    public ItemWriter<Timetable> itemWriter() {
        return new TimetableItemWriter(routeRepository);
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new RemoveRouteLinesExecutionListener(routeRepository, environment);
    }

    @Bean
    public StringCleaner stringCleaner() {
        return new StringCleaner();
    }
}
