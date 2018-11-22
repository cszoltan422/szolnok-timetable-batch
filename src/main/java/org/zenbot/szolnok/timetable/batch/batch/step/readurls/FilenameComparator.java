package org.zenbot.szolnok.timetable.batch.batch.step.readurls;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

@Component
public class FilenameComparator implements Comparator<File>, Serializable {

    private static final long serialVersionUID = 6106269076155338045L;

    @Override
    public int compare(File first, File second) {
        String firstFilename = first.getName();
        String secondFilename = second.getName();
        int a = getNumber(firstFilename);
        int b = getNumber(secondFilename);
        if (a != b) {
            return Integer.compare(a, b);
        } else {
            if (firstFilename.length() != secondFilename.length()) {
                return Integer.compare(firstFilename.length(), secondFilename.length());
            } else {
                return firstFilename.compareTo(secondFilename);
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
}
