package org.zenbot.timetableupdater.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ResourceReader {

    private final Environment environment;
    private final FilenameComparator comparator;
    private final TimetableResourceLocationProperties properties;

    public ResourceReader(Environment environment, FilenameComparator comparator, TimetableResourceLocationProperties properties) {
        this.environment = environment;
        this.comparator = comparator;
        this.properties = properties;
    }

    public List<Resource> readResources() throws IOException {
        File resourceDirectory = new File(this.getClass().getResource(File.separator + properties.getFolder()).getFile());
        if (!resourceDirectory.isDirectory()) {
            throw new IllegalStateException("Must be a directory");
        }

        log.info("Reading resources from [{}]", resourceDirectory.getAbsolutePath());

        String[] activeProfiles = environment.getActiveProfiles();
        log.info("Active profiles [{}]", String.join(",", Arrays.asList(activeProfiles)));
        return read(resourceDirectory, Arrays.asList(activeProfiles));
    }



    private List<Resource> read(File resourceDirectory, List<String> profiles) throws IOException {
        List<Resource> resources = new ArrayList<>();
        List<File> fileResources = getFileResourcesByActiveProfiles(profiles, resourceDirectory);
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

    private List<File> getFileResourcesByActiveProfiles(List<String> profiles, File resourceDirectory) {
        List<String> activeProfileFilesNames = profiles.stream()
                .map(activeProfile -> activeProfile.concat(properties.getFileExtension()))
                .collect(Collectors.toList());

        File[] files = resourceDirectory.listFiles();
        List<File> result = new ArrayList<>();

        if (profiles.size() > 0) {
            result.addAll(Arrays.stream(files)
                    .filter(file -> activeProfileFilesNames.contains(file.getName()))
                    .collect(Collectors.toList()));
        } else {
            result.addAll(Arrays.asList(files));
        }

        Collections.sort(result, comparator);
        return result;
    }
}
