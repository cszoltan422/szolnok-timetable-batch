package org.zenbot.timetableupdater.batch;

public class StringCleaner {
    public String clean(String string) {
        string = string.replaceAll("û", "ű");
        string = string.replaceAll("õ", "ő");
        string = string.replaceAll("ô", "ő");
        return string;
    }
}
