package org.zenbot.szolnok.timetable.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableResourceProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ResourceReader {

    private final FilenameComparator comparator;
    private final TimetableResourceProperties properties;

    public ResourceReader(FilenameComparator comparator, TimetableResourceProperties properties) {
        this.comparator = comparator;
        this.properties = properties;
    }

    public List<Resource> readResources() throws IOException {
        log.info("{}",properties.getSelectedBuses());
        File resourceDirectory = new File(this.getClass().getResource(File.separator + properties.getFolder()).getFile());
        log.info("{}", resourceDirectory.getAbsolutePath());
        if (!resourceDirectory.isDirectory()) {
            log.error("Not a directory [{}]", resourceDirectory.getAbsolutePath());
            throw new IllegalStateException("Must be a directory");
        }

        log.info("Reading resources from [{}]", resourceDirectory.getAbsolutePath());

        log.info("Selected buses [{}]", String.join(",", properties.getSelectedBuses()));
        return read(resourceDirectory, properties.getSelectedBuses());
    }



    private List<Resource> read(File resourceDirectory, List<String> profiles) throws IOException {
        List<Resource> resources = new ArrayList<>();
        List<File> fileResources = getFileResourcesBySelectedBuses(profiles, resourceDirectory);
        for (File file : fileResources) {
            List<UrlResource> urlResources = Arrays.stream(FileUtils.readFileToString(file, "UTF-8")
                    .split(System.lineSeparator()))
                    .filter(line -> !line.isEmpty() && !line.startsWith(properties.getCommentSign()))
                    .map(line -> {
                        try {
                            return new UrlResource(line);
                        } catch (MalformedURLException e) {
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
            resources.addAll(urlResources);
        }

        return resources;
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

        Collections.sort(result, comparator);
        return result;
    }
}
