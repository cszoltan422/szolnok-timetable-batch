package org.zenbot.szolnok.timetable.batch.batch.step.readurls;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableProperties;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableResourceProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableConfigurationProperties(TimetableProperties.class)
public class ReadFileResourcesTasklet implements Tasklet {

    private final StringResourcesInMemoryStorage stringResourcesInMemoryStorage;
    private final FilenameComparator comparator;
    private final TimetableResourceProperties properties;

    public ReadFileResourcesTasklet(StringResourcesInMemoryStorage stringResourcesInMemoryStorage, FilenameComparator comparator, TimetableProperties properties) {
        this.stringResourcesInMemoryStorage = stringResourcesInMemoryStorage;
        this.comparator = comparator;
        this.properties = properties.getResource();
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("{}",properties.getSelectedBuses());
        File resourceDirectory = new File(this.getClass().getResource(File.separator + properties.getFolder()).getFile());
        log.info("{}", resourceDirectory.getAbsolutePath());
        if (!resourceDirectory.isDirectory()) {
            log.error("Not a directory [{}]", resourceDirectory.getAbsolutePath());
            throw new IllegalStateException("Must be a directory");
        }

        log.info("Reading resources from [{}]", resourceDirectory.getAbsolutePath());

        log.info("Selected buses [{}]", String.join(",", properties.getSelectedBuses()));

        read(resourceDirectory, properties.getSelectedBuses());
        return RepeatStatus.FINISHED;
    }

    private void read(File resourceDirectory, List<String> profiles) throws IOException {
        List<File> fileResources = getFileResourcesBySelectedBuses(profiles, resourceDirectory);
        for (File file : fileResources) {
            List<String> urlResources = Arrays.stream(FileUtils.readFileToString(file, "UTF-8")
                    .split(System.lineSeparator()))
                    .filter(line -> !line.isEmpty() && !line.startsWith(properties.getCommentSign()))
                    .collect(Collectors.toList());
            urlResources.forEach(stringResourcesInMemoryStorage::addUrl);
        }

    }

    private List<File> getFileResourcesBySelectedBuses(List<String> selectedBuses, File resourceDirectory) {
        List<File> result = new ArrayList<>();
        File[] files = resourceDirectory.listFiles();
        if (selectedBuses.isEmpty()) {
            result.addAll(Arrays.stream(files).collect(Collectors.toList()));
        } else {
            List<String> selectedBusesFilenames = selectedBuses.stream()
                    .map(bus -> bus.concat("." + properties.getFileExtension()))
                    .collect(Collectors.toList());
            log.info("{}",selectedBusesFilenames);
            result.addAll(
                    Arrays.stream(files)
                            .filter(file -> selectedBusesFilenames.contains(file.getName()))
                            .collect(Collectors.toList())
            );
        }

        result.sort(comparator);
        return result;
    }
}
