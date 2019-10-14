package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

class TimetableRowBuilderItemProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject: TimetableRowBuilderItemProcessorHelper

    @Mock
    private lateinit var htmlTimetableProcessorHelper: HtmlTimetableProcessorHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getTimetableRows should call the HtmlTimetableProcessorHelper per table`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.timetableSelector = "timetableSelector"
        val tables = mock(Elements::class.java)
        val table1 = mock(Element::class.java)
        val table2 = mock(Element::class.java)
        val elements = mutableListOf(table1, table2)

        val mappedTable1 = mapOf(1 to mapOf("sunday" to "1,2,3"))
        val mappedTable2 = mapOf(2 to mapOf("workday" to "1,2,3"))

        given(htmlDocument.select(anyString())).willReturn(tables)
        given(tables.iterator()).willReturn(elements.iterator())
        given(htmlTimetableProcessorHelper.processHtmlTable(table1, timetableSelectorProperties)).willReturn(mappedTable1)
        given(htmlTimetableProcessorHelper.processHtmlTable(table2, timetableSelectorProperties)).willReturn(mappedTable2)

        // WHEN
        val result = testSubject.getTimetableRows(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select("timetableSelector")
        verify(tables).iterator()
        verify(htmlTimetableProcessorHelper).processHtmlTable(table1, timetableSelectorProperties)
        verify(htmlTimetableProcessorHelper).processHtmlTable(table2, timetableSelectorProperties)

        assertThat(result).isNotNull()
                .containsKey(1)
                .containsKey(2)
                .containsEntry(1, mapOf("sunday" to "1,2,3"))
                .containsEntry(2, mapOf("workday" to "1,2,3"))
    }
}