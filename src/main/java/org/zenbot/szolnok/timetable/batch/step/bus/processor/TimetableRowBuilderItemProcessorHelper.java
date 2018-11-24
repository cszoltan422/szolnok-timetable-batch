package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import com.google.common.collect.ImmutableMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

import java.util.HashMap;
import java.util.Map;

import static org.zenbot.szolnok.timetable.batch.step.bus.processor.JsoupDocumentToTimetableProcessor.*;

@Component
public class TimetableRowBuilderItemProcessorHelper {
    public Map<Integer, Map<String, String>> getTimetableRows(Document htmlDocument, TimetableSelectorProperties selectorProperties) {
        Map<Integer, Map<String, String>> result = new HashMap<>();
        for (Element table : htmlDocument.select(selectorProperties.getTimetableSelector())) {
            for (Element row : table.select(selectorProperties.getTableRowSelector())) {
                Elements tds = row.select(selectorProperties.getTableColumnSelector());
                if (tds.size() == 4) {
                    String hour = tds.get(0).text();
                    String weekdayArrivals = tds.get(1).text().replaceAll(" ", "");
                    String saturdayArrivals = tds.get(2).text().replaceAll(" ", "");
                    String sundayArrivals = tds.get(3).text().replaceAll(" ", "");
                    result.put(Integer.parseInt(hour), ImmutableMap.<String, String>builder()
                            .put(WEEKDAY_KEY, weekdayArrivals)
                            .put(SATURDAY_KEY, saturdayArrivals)
                            .put(SUNDAY_KEY, sundayArrivals)
                            .build());
                }
            }
        }
        return result;
    }
}
