package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.ActualStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.BusNameSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.StartBusStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.TerminalSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.TimetableRowBuilderItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

class JsoupDocumentToTimetableProcessorTest {

    private lateinit var testSubject: JsoupDocumentToTimetableProcessor

    private lateinit var properties: TimetableProperties

    @Mock
    private lateinit var busNameSelectorItemProcessorHelper: BusNameSelectorItemProcessorHelper

    @Mock
    private lateinit var startBusStopSelectorItemProcessorHelper: StartBusStopSelectorItemProcessorHelper

    @Mock
    private lateinit var terminalSelectorItemProcessorHelper: TerminalSelectorItemProcessorHelper

    @Mock
    private lateinit var actualStopSelectorItemProcessorHelper: ActualStopSelectorItemProcessorHelper

    @Mock
    private lateinit var timetableRowBuilderItemProcessorHelper: TimetableRowBuilderItemProcessorHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        properties = TimetableProperties()
        testSubject = JsoupDocumentToTimetableProcessor(
                properties,
                busNameSelectorItemProcessorHelper,
                startBusStopSelectorItemProcessorHelper,
                terminalSelectorItemProcessorHelper,
                actualStopSelectorItemProcessorHelper,
                timetableRowBuilderItemProcessorHelper
        )
    }

    @Test
    fun `process should use it's dependencies to create the timetable`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val timetableMap = mapOf(1 to mapOf("weekday" to "1,2"))
        val selector = TimetableSelectorProperties()
        properties.selector = selector

        given(busNameSelectorItemProcessorHelper.getBusName(htmlDocument, properties.selector)).willReturn("busName")
        given(startBusStopSelectorItemProcessorHelper.getStartBusStop(htmlDocument, properties.selector)).willReturn("startStop")
        given(terminalSelectorItemProcessorHelper.getTerminal(htmlDocument, properties.selector)).willReturn("terminal")
        given(actualStopSelectorItemProcessorHelper.getActualStop(htmlDocument, properties.selector)).willReturn("actual")
        given(timetableRowBuilderItemProcessorHelper.getTimetableRows(htmlDocument, properties.selector)).willReturn(timetableMap)

        // WHEN
        val result = testSubject.process(htmlDocument)

        // THEN
        verify(busNameSelectorItemProcessorHelper).getBusName(htmlDocument, properties.selector)
        verify(startBusStopSelectorItemProcessorHelper).getStartBusStop(htmlDocument, properties.selector)
        verify(terminalSelectorItemProcessorHelper).getTerminal(htmlDocument, properties.selector)
        verify(actualStopSelectorItemProcessorHelper).getActualStop(htmlDocument, properties.selector)
        verify(timetableRowBuilderItemProcessorHelper).getTimetableRows(htmlDocument, properties.selector)

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("busName", "busName")
                .hasFieldOrPropertyWithValue("startBusStopName", "startStop")
                .hasFieldOrPropertyWithValue("endBusStopName", "terminal")
                .hasFieldOrPropertyWithValue("activeStopName", "actual")
                .hasFieldOrPropertyWithValue("timetable", mutableMapOf(1 to mapOf("weekday" to "1,2")))
    }

}