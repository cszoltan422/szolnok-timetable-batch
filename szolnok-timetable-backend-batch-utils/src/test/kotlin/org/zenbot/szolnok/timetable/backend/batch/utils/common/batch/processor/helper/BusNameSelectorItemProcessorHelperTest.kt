package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

class BusNameSelectorItemProcessorHelperTest {

    private lateinit var testSubject: BusNameSelectorItemProcessorHelper

    @Before
    fun setup() {
        testSubject = BusNameSelectorItemProcessorHelper()
    }

    @Test
    fun `getBusName should select the bus name from the document`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.routeNameSelector = "routeNameSelector"
        val busnameElement = mock(Elements::class.java)

        given(htmlDocument.select(anyString())).willReturn(busnameElement)
        given(busnameElement.text()).willReturn("busName")

        // WHEN
        val result = testSubject.getBusName(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select("routeNameSelector")
        verify(busnameElement).text()

        assertThat(result).isNotNull()
                .isEqualTo("busName")
    }
}