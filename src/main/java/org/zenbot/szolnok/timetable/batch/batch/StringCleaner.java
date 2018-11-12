package org.zenbot.szolnok.timetable.batch.batch;

public class StringCleaner {
    public String clean(String string) {
        return string
                .replaceAll("û", "ű")
                .replaceAll("õ", "ő")
                .replaceAll("ô", "ő");
    }
}
