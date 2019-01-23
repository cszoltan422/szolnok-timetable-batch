package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;
import org.zenbot.szolnok.timetable.domain.Timetable;

import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(TimetableProperties.class)
public class JsoupDocumentToTimetableProcessor implements ItemProcessor<Document, Timetable> {

    public static final String WEEKDAY_KEY = "weekday";
    public static final String SATURDAY_KEY = "saturday";
    public static final String SUNDAY_KEY = "sunday";

    private final TimetableSelectorProperties selectorProperties;

    private final EndBusStopSelectorItemProcessorHelper endBusStopSelectorItemProcessorHelper;
    private final ActualStopSelectorItemProcessorHelper actualStopSelectorItemProcessorHelper;
    private final TimetableRowBuilderItemProcessorHelper timetableRowBuilderItemProcessorHelper;

    public JsoupDocumentToTimetableProcessor(TimetableProperties properties, EndBusStopSelectorItemProcessorHelper endBusStopSelectorItemProcessorHelper, ActualStopSelectorItemProcessorHelper actualStopSelectorItemProcessorHelper, TimetableRowBuilderItemProcessorHelper timetableRowBuilderItemProcessorHelper) {
        this.selectorProperties = properties.getSelector();
        this.endBusStopSelectorItemProcessorHelper = endBusStopSelectorItemProcessorHelper;
        this.actualStopSelectorItemProcessorHelper = actualStopSelectorItemProcessorHelper;
        this.timetableRowBuilderItemProcessorHelper = timetableRowBuilderItemProcessorHelper;
    }

    @Override
    public Timetable process(Document htmlDocument) {
        Timetable timetable = new Timetable();
        String busName = htmlDocument.select(selectorProperties.getRouteNameSelector()).text();
        String startBusStop = htmlDocument.select(selectorProperties.getFromSelector()).text();
        String endBusStop = endBusStopSelectorItemProcessorHelper.getEndBusStop(htmlDocument, selectorProperties);
        String actualStop = actualStopSelectorItemProcessorHelper.getActualStop(htmlDocument, selectorProperties);
        Map<Integer, Map<String, String>> timetableRows = timetableRowBuilderItemProcessorHelper.getTimetableRows(htmlDocument, selectorProperties);

        timetable.setBusName(busName);
        timetable.setStartBusStopName(startBusStop);
        timetable.setEndBusStopName(endBusStop);
        timetable.setActiveStopName(actualStop);
        timetableRows.forEach(timetable::addRow);

        log.info("Process html with busName [#{}] and timetable for [{}] stop", timetable.getBusName(), timetable.getActiveStopName());
        return timetable;
    }

}
