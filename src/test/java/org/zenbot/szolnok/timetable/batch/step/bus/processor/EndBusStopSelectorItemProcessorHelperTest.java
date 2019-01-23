package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class EndBusStopSelectorItemProcessorHelperTest {

    private static final String BUS_STOPS_SELECTOR = "BUS_STOPS_SELECTOR";
    private static final String END_BUS_STOP_NAME = "END_BUS_STOP_NAME";
    private static final String TABLE_ROW_SELECTOR = "TABLE_ROW_SELECTOR";
    private static final String TABLE_COLUMN_SELECTOR = "TABLE_COLUMN_SELECTOR";
    private static final String CLEARED_STRING = "CLEARED_STRING";

    private EndBusStopSelectorItemProcessorHelper testSubject;

    @Mock
    private StringCleaner stringCleaner;

    @Mock
    private Document document;

    @Mock
    private Elements stationsTable;

    @Mock
    private Elements rows;

    @Mock
    private Element lastRow;

    @Mock
    private Elements lastRowColumns;

    @Mock
    private Element lastColumn;

    private TimetableSelectorProperties properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        properties = new TimetableSelectorProperties();
        properties.setBusStopsSelector(BUS_STOPS_SELECTOR);
        properties.setTableRowSelector(TABLE_ROW_SELECTOR);
        properties.setTableColumnSelector(TABLE_COLUMN_SELECTOR);

        testSubject = new EndBusStopSelectorItemProcessorHelper(stringCleaner);
    }

    @Test
    public void getEndBusStopShouldWork() {
        // GIVEN
        given(document.select(anyString())).willReturn(stationsTable);
        given(stationsTable.select(anyString())).willReturn(rows);
        given(rows.size()).willReturn(3);
        given(rows.get(anyInt())).willReturn(lastRow);
        given(lastRow.select(anyString())).willReturn(lastRowColumns);
        given(lastRowColumns.get(anyInt())).willReturn(lastColumn);
        given(lastColumn.text()).willReturn(END_BUS_STOP_NAME);
        given(stringCleaner.clean(anyString())).willReturn(CLEARED_STRING);

        // WHEN
        String result = testSubject.getEndBusStop(document, properties);

        //THEN
        verify(document).select(BUS_STOPS_SELECTOR);
        verify(stationsTable).select(TABLE_ROW_SELECTOR);
        verify(rows).get(1);
        verify(lastRow).select(TABLE_COLUMN_SELECTOR);
        verify(lastRowColumns).get(2);
        verify(stringCleaner).clean(END_BUS_STOP_NAME);

        assertThat(result).isEqualTo(CLEARED_STRING);
    }
}