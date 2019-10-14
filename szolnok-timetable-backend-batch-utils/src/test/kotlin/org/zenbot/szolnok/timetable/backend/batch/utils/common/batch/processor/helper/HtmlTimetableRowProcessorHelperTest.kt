package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

class HtmlTimetableRowProcessorHelperTest {

    private lateinit var testSubject: HtmlTimetableRowProcessorHelper

    @Before
    fun setUp() {
        testSubject = HtmlTimetableRowProcessorHelper()
    }

    @Test
    fun `processRow should return an empy map if the number of columns in the row is not 4`() {
        // GIVEN
        val row = mock(Element::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.tableColumnSelector = "tableColumnSelector"
        val tds = mock(Elements::class.java)

        given(row.select(anyString())).willReturn(tds)
        given(tds.size).willReturn(3)

        // WHEN
        val result = testSubject.processRow(row, timetableSelectorProperties)

        // THEN
        verify(row).select("tableColumnSelector")
        verify(tds).size

        assertThat(result)
                .isNotNull()
                .isEmpty()
    }

    @Test
    fun `processRow should map a valid row into a map and remove thw whitespaces`() {
        val row = mock(Element::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.tableColumnSelector = "tableColumnSelector"
        val tds = mock(Elements::class.java)
        val column1 = mock(Element::class.java)
        val column2 = mock(Element::class.java)
        val column3 = mock(Element::class.java)
        val column4 = mock(Element::class.java)

        given(row.select(anyString())).willReturn(tds)
        given(tds.size).willReturn(4)
        given(tds[0]).willReturn(column1)
        given(tds[1]).willReturn(column2)
        given(tds[2]).willReturn(column3)
        given(tds[3]).willReturn(column4)

        given(column1.text()).willReturn("1")
        given(column2.text()).willReturn("1, 2, 3")
        given(column3.text()).willReturn("1, 4, 5")
        given(column4.text()).willReturn("10, 11")

        // WHEN
        val result = testSubject.processRow(row, timetableSelectorProperties)

        // THEN
        verify(row).select("tableColumnSelector")
        verify(tds).size
        verify(tds)[0]
        verify(tds)[1]
        verify(tds)[2]
        verify(tds)[3]
        verify(column1).text()
        verify(column2).text()
        verify(column3).text()
        verify(column4).text()

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .containsEntry(
                        1,
                        mapOf(
                                JsoupDocumentToTimetableProcessor.WEEKDAY_KEY to "1,2,3",
                                JsoupDocumentToTimetableProcessor.SATURDAY_KEY to "1,4,5",
                                JsoupDocumentToTimetableProcessor.SUNDAY_KEY to "10,11"
                        )
                )
    }
}
