package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

class HtmlTimetableProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject: HtmlTimetableProcessorHelper

    @Mock
    private lateinit var htmlTimetableRowProcessorHelper: HtmlTimetableRowProcessorHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `processHtmlTable should call the htmlTimetableRowProcessorHelper per each row in table`() {
        // GIVEN
        val table = Mockito.mock(Element::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.tableRowSelector = "tableRowSelector"

        val rows = Mockito.mock(Elements::class.java)
        val row1 = Mockito.mock(Element::class.java)
        val row2 = Mockito.mock(Element::class.java)
        val elements = mutableListOf(row1, row2)

        val mappedRow1 = mapOf(1 to mapOf("sunday" to "1,2,3"))
        val mappedRow2 = mapOf(2 to mapOf("workday" to "1,2,3"))

        given(table.select(anyString())).willReturn(rows)
        given(rows.iterator()).willReturn(elements.iterator())
        given(htmlTimetableRowProcessorHelper.processRow(row1, timetableSelectorProperties)).willReturn(mappedRow1)
        given(htmlTimetableRowProcessorHelper.processRow(row2, timetableSelectorProperties)).willReturn(mappedRow2)

        // WHEN
        val result = testSubject.processHtmlTable(table, timetableSelectorProperties)

        // THEN
        verify(table).select("tableRowSelector")
        verify(rows).iterator()
        verify(htmlTimetableRowProcessorHelper).processRow(row1, timetableSelectorProperties)
        verify(htmlTimetableRowProcessorHelper).processRow(row2, timetableSelectorProperties)

        Assertions.assertThat(result).isNotNull()
                .containsKey(1)
                .containsKey(2)
                .containsEntry(1, mapOf("sunday" to "1,2,3"))
                .containsEntry(2, mapOf("workday" to "1,2,3"))
    }
}