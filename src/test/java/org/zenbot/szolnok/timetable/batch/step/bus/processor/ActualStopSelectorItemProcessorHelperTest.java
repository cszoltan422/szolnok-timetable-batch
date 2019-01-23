package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ActualStopSelectorItemProcessorHelperTest {

    private static final String SELECTOR = "SELECTOR";
    private static final String BUS_STOP_NAME = "(BUS_STOP_NAME)";
    private static final String EXPECTED_BUS_STOP_NAME = "BUS_STOP_NAME";
    private static final String CLEARED_STRING = "CLEARED_STRING";
    private static final String BUS_STOP_NAME_WITH_PARENTHESIS = "(BUS_STOP (NAME))";
    private static final String EXPECTED_BUS_STOP_NAME_WITH_PARENTHESIS = "BUS_STOP (NAME)";
    private static final String BUS_STOP_NAME_WITH_DOT = "(BUS_STOP_NAME.)";

    private ActualStopSelectorItemProcessorHelper testSubject;

    private TimetableSelectorProperties selectorProperties;

    @Mock
    private StringCleaner stringCleaner;

    @Mock
    private Document document;

    @Mock
    private Elements element;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        selectorProperties = new TimetableSelectorProperties();
        selectorProperties.setActualStopSelector(SELECTOR);

        testSubject = new ActualStopSelectorItemProcessorHelper(stringCleaner);
    }

    @Test
    public void getActualStopShouldRemoveParenthesis() {
        // GIVEN
        given(element.text()).willReturn(BUS_STOP_NAME);
        given(document.select(SELECTOR)).willReturn(element);
        given(stringCleaner.clean(anyString())).willReturn(CLEARED_STRING);

        // WHEN
        String result = testSubject.getActualStop(document, selectorProperties);

        //THEN
        assertThat(result).isEqualTo(CLEARED_STRING);

        verify(element).text();
        verify(document).select(SELECTOR);
        verify(stringCleaner).clean(EXPECTED_BUS_STOP_NAME);
    }

    @Test
    public void getActualStopShouldReturnWithParenthesisIfBusStopNameHad() {
        // GIVEN
        given(element.text()).willReturn(BUS_STOP_NAME_WITH_PARENTHESIS);
        given(document.select(SELECTOR)).willReturn(element);
        given(stringCleaner.clean(anyString())).willReturn(CLEARED_STRING);

        // WHEN
        String result = testSubject.getActualStop(document, selectorProperties);

        //THEN
        verify(element).text();
        verify(document).select(SELECTOR);
        verify(stringCleaner).clean(EXPECTED_BUS_STOP_NAME_WITH_PARENTHESIS);

    }

    @Test
    public void getActualStopShouldRemoveDots() {
        given(element.text()).willReturn(BUS_STOP_NAME_WITH_DOT);
        given(document.select(SELECTOR)).willReturn(element);
        given(stringCleaner.clean(anyString())).willReturn(CLEARED_STRING);

        // WHEN
        String result = testSubject.getActualStop(document, selectorProperties);

        //THEN
        verify(element).text();
        verify(document).select(SELECTOR);
        verify(stringCleaner).clean(EXPECTED_BUS_STOP_NAME);
    }

}