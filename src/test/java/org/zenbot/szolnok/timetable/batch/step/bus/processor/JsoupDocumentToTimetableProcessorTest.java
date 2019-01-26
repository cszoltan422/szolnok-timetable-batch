package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;
import org.zenbot.szolnok.timetable.domain.Timetable;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class JsoupDocumentToTimetableProcessorTest {

    private static final String ROUTE_NAME_SELECTOR = "ROUTE_NAME_SELECTOR";
    private static final String FROM_SELECTOR = "FROM_SELECTOR";
    private static final String ACTUAL_STOP_SELECTOR = "ACTUAL_STOP_SELECTOR";
    private static final String ROUTE_NAME = "ROUTE_NAME";
    private static final String START_BUS_STOP = "START_BUS_STOP";
    private static final String END_BUS_STOP = "END_BUS_STOP";
    private static final String ACTUAL_STOP = "ACTUAL_STOP";

    private JsoupDocumentToTimetableProcessor testSubject;

    private TimetableSelectorProperties selectorProperties;

    @Mock
    private Document html;

    @Mock
    private Elements routeNameElement;

    @Mock
    private Elements startBusStopElement;

    @Mock
    private StartBusStopSelectorItemProcessorHelper startBusStopSelectorItemProcessorHelper;

    @Mock
    private EndBusStopSelectorItemProcessorHelper endBusStopSelectorItemProcessorHelper;

    @Mock
    private ActualStopSelectorItemProcessorHelper actualStopSelectorItemProcessorHelper;

    @Mock
    private TimetableRowBuilderItemProcessorHelper timetableRowBuilderItemProcessorHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        selectorProperties = new TimetableSelectorProperties();
        selectorProperties.setRouteNameSelector(ROUTE_NAME_SELECTOR);
        selectorProperties.setFromSelector(FROM_SELECTOR);
        selectorProperties.setActualStopSelector(ACTUAL_STOP_SELECTOR);
        TimetableProperties properties = new TimetableProperties();
        properties.setSelector(selectorProperties);

        testSubject = new JsoupDocumentToTimetableProcessor(properties, startBusStopSelectorItemProcessorHelper, endBusStopSelectorItemProcessorHelper, actualStopSelectorItemProcessorHelper, timetableRowBuilderItemProcessorHelper);
    }

    @Test
    public void processShouldDelegateCallsToHelpers() {
        // GIVEN
        given(html.select(selectorProperties.getRouteNameSelector())).willReturn(routeNameElement);
        given(routeNameElement.text()).willReturn(ROUTE_NAME);
        given(startBusStopSelectorItemProcessorHelper.getStartBusStop(html, selectorProperties)).willReturn(START_BUS_STOP);
        given(endBusStopSelectorItemProcessorHelper.getEndBusStop(any(Document.class), any(TimetableSelectorProperties.class))).willReturn(END_BUS_STOP);
        given(actualStopSelectorItemProcessorHelper.getActualStop(any(Document.class), any(TimetableSelectorProperties.class))).willReturn(ACTUAL_STOP);
        Map<Integer, Map<String, String>> timetable = new HashMap<>();
        given(timetableRowBuilderItemProcessorHelper.getTimetableRows(any(Document.class), any(TimetableSelectorProperties.class))).willReturn(timetable);

        // WHEN
        Timetable result = testSubject.process(html);

        //THEN
        verify(html).select(ROUTE_NAME_SELECTOR);
        verify(startBusStopSelectorItemProcessorHelper).getStartBusStop(html, selectorProperties);
        verify(endBusStopSelectorItemProcessorHelper).getEndBusStop(html, selectorProperties);
        verify(actualStopSelectorItemProcessorHelper).getActualStop(html, selectorProperties);
        verify(timetableRowBuilderItemProcessorHelper).getTimetableRows(html, selectorProperties);

        assertThat(result).isNotNull();
        assertThat(result.getBusName()).isEqualTo(ROUTE_NAME);
        assertThat(result.getStartBusStopName()).isEqualTo(START_BUS_STOP);
        assertThat(result.getActiveStopName()).isEqualTo(ACTUAL_STOP);
        assertThat(result.getEndBusStopName()).isEqualTo(END_BUS_STOP);
        assertThat(result.getTimetable()).isEqualTo(timetable);

    }

}