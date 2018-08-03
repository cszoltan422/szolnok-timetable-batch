package org.zenbot.timetableupdater.configuration;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Comparator;

@Component
public class FilenameComparator implements Comparator<File> {
    
    @Override
    public int compare(File first, File second) {
        String firstFilename = first.getName();
        String secondFilename = second.getName();
        int a = getNumber(firstFilename);
        int b = getNumber(secondFilename);
        if (a != b) {
            return Integer.compare(a, b) * 2; // "Stronger" comparision
        } else {
            if (firstFilename.length() != secondFilename.length()) {
                return Integer.compare(firstFilename.length(), secondFilename.length());
            } else {
                return compareLastChars(firstFilename, secondFilename);
            }
        }
    }

    private int getNumber(String filename) {
        StringBuilder builder = new StringBuilder();
        for (char c : filename.toCharArray()) {
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return Integer.parseInt(builder.toString());
    }

    private int compareLastChars(String first, String second) {
        return first.substring(first.length() - 1).compareTo(second.substring(second.length() - 1));
    }
}
