package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

class TerminalSelectorItemProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject: TerminalSelectorItemProcessorHelper

    @Mock
    private lateinit var stringCleaner: StringCleaner

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getTerminal should select the last bus stop from the html document`() {
        // GIVEN
        val htmlDocument = Mockito.mock(Document::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.busStopsSelector = "busStopsSelector"
        timetableSelectorProperties.tableRowSelector = "tableRowSelector"
        timetableSelectorProperties.tableColumnSelector = "tableColumnSelector"
        val busStopsTable = Mockito.mock(Elements::class.java)
        val busStopRows = Mockito.mock(Elements::class.java)
        val terminalStopRow = Mockito.mock(Element::class.java)
        val columns = Mockito.mock(Elements::class.java)
        val lastColumn = Mockito.mock(Element::class.java)

        given(htmlDocument.select(ArgumentMatchers.anyString())).willReturn(busStopsTable)
        given(busStopsTable.select(ArgumentMatchers.anyString())).willReturn(busStopRows)
        given(busStopRows.size).willReturn(10)
        given(busStopRows[ArgumentMatchers.anyInt()]).willReturn(terminalStopRow)
        given(terminalStopRow.select(ArgumentMatchers.anyString())).willReturn(columns)
        given(columns[ArgumentMatchers.anyInt()]).willReturn(lastColumn)
        given(lastColumn.text()).willReturn("terminal")
        given(stringCleaner.clean(ArgumentMatchers.anyString())).willReturn("cleaned")

        // WHEN
        val result = testSubject.getTerminal(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select(timetableSelectorProperties.busStopsSelector)
        verify(busStopsTable).select(timetableSelectorProperties.tableRowSelector)
        verify(busStopRows).size
        verify(busStopRows)[8]
        verify(terminalStopRow).select(timetableSelectorProperties.tableColumnSelector)
        verify(columns)[2]
        verify(lastColumn).text()
        verify(stringCleaner).clean("terminal")

        assertThat(result).isEqualTo("cleaned")
    }
}