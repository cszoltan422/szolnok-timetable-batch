package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

class StartBusStopSelectorItemProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject: StartBusStopSelectorItemProcessorHelper

    @Mock
    private lateinit var stringCleaner: StringCleaner

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getStartBusStop should select the start stop from the html document`() {
        // GIVEN
        val htmlDocument = Mockito.mock(Document::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.fromSelector = "fromSelector"
        val elements: Elements = mock(Elements::class.java)

        given(htmlDocument.select(anyString())).willReturn(elements)
        given(elements.text()).willReturn("startStop")
        given(stringCleaner.clean(anyString())).willReturn("cleaned")

        // WHEN
        val result = testSubject.getStartBusStop(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select(timetableSelectorProperties.fromSelector)
        verify(elements).text()
        verify(stringCleaner).clean("startStop")

        assertThat(result).isNotNull()
                .isEqualTo("cleaned")
    }
}
