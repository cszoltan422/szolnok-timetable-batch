package org.zenbot.szolnok.timetable.batch.batch;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemProcessor;
import org.zenbot.szolnok.timetable.batch.configuration.properties.TimetableSelectorProperties;

@Slf4j
public class TimetableProcessor implements ItemProcessor<Document, Timetable> {

    public static final String WEEKDAY_KEY = "weekday";
    public static final String SATURDAY_KEY = "saturday";
    public static final String SUNDAY_KEY = "sunday";

    private final StringCleaner stringCleaner;
    private final TimetableSelectorProperties selectorProperties;

    public TimetableProcessor(StringCleaner stringCleaner, TimetableSelectorProperties selectorProperties) {
        this.stringCleaner = stringCleaner;
        this.selectorProperties = selectorProperties;
    }

    @Override
    public Timetable process(Document htmlDocument) {
        Timetable timetable = new Timetable();
        setBusName(htmlDocument, timetable);
        setStartBusStopName(htmlDocument, timetable);
        setEndBusStopName(htmlDocument, timetable);
        setActiveBusStopName(htmlDocument, timetable);
        setTimetable(htmlDocument, timetable);

        log.info("Process html with busName [#{}] and timetable for [{}] stop", timetable.getBusName(), timetable.getActiveStopName());
        return timetable;
    }

    private void setTimetable(Document htmlDocument, Timetable timetable) {
        for (Element table : htmlDocument.select(selectorProperties.getTimetableSelector())) {
            for (Element row : table.select(selectorProperties.getTableRowSelector())) {
                Elements tds = row.select(selectorProperties.getTableColumnSelector());
                if (tds.size() == 4) {
                    String hour = tds.get(0).text();
                    String weekdayArrivals = tds.get(1).text().replaceAll(" ", "");
                    String saturdayArrivals = tds.get(2).text().replaceAll(" ", "");
                    String sundayArrivals = tds.get(3).text().replaceAll(" ", "");
                    timetable.addRow(Integer.valueOf(hour), ImmutableMap.<String, String>builder()
                            .put(WEEKDAY_KEY, weekdayArrivals)
                            .put(SATURDAY_KEY, saturdayArrivals)
                            .put(SUNDAY_KEY, sundayArrivals)
                            .build());
                }
            }
        }
    }

    private void setActiveBusStopName(Document htmlDocument, Timetable timetable) {
        String actualStop = getHtmlText(htmlDocument, selectorProperties.getActualStopSelector());
        // In the html page they put it insede () signs.
        actualStop = (String) actualStop.subSequence(actualStop.indexOf("(") + 1, actualStop.indexOf(")"));
        if (actualStop.contains("(")) {
            actualStop += ")";
        }
        if (actualStop.endsWith(".")) {
            actualStop = actualStop.substring(0, actualStop.length() - 1);
        }
        timetable.setActiveStopName(stringCleaner.clean(actualStop));
    }

    private void setEndBusStopName(Document htmlDocument, Timetable timetable) {
        Elements stationsTable = htmlDocument.select(selectorProperties.getBusStopsSelector());
        stationsTable.select(selectorProperties.getTableRowSelector()).size();
        int indexOfEndBusStop = stationsTable.select(selectorProperties.getTableRowSelector()).size() - 2;
        Elements rows = stationsTable.select(selectorProperties.getTableRowSelector());
        String to = rows.get(indexOfEndBusStop).select(selectorProperties.getTableColumnSelector()).get(2).text();
        timetable.setEndBusStopName(stringCleaner.clean(to));
    }

    private void setStartBusStopName(Document htmlDocument, Timetable timetable) {
        String from = getHtmlText(htmlDocument, selectorProperties.getFromSelector());
        timetable.setStartBusStopName(stringCleaner.clean(from));
    }

    private void setBusName(Document htmlDocument, Timetable timetable) {
        String routename = getHtmlText(htmlDocument, selectorProperties.getRouteNameSelector());
        timetable.setBusName(routename);
    }

    private String getHtmlText(Document htmlDocument, String s) {
        return htmlDocument.select(s).text();
    }
}
