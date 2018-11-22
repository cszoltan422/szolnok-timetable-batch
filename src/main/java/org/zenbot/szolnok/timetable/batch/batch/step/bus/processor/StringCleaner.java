package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import org.springframework.stereotype.Component;

@Component
public class StringCleaner {
    public String clean(String string) {
        return string
                .replaceAll("û", "ű")
                .replaceAll("õ", "ő")
                .replaceAll("ô", "ő");
    }
}
