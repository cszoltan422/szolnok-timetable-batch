package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
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
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

class ActualStopSelectorItemProcessorHelperTest {

    @InjectMocks
    private lateinit var testSubject : ActualStopSelectorItemProcessorHelper

    @Mock
    private lateinit var stringCleaner: StringCleaner

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getActualStop should remove the parentheses around the string value and clean it with the cleaner`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val elements = mock(Elements::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.actualStopSelector = "selector"

        given(htmlDocument.select(anyString())).willReturn(elements)
        given(elements.text()).willReturn("(STOP)")
        given(stringCleaner.clean(anyString())).willReturn("cleaned")

        // WHEN
        val result = testSubject.getActualStop(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select(timetableSelectorProperties.actualStopSelector)
        verify(elements).text()
        verify(stringCleaner).clean("STOP")

        assertThat(result).isEqualTo("cleaned")
    }

    @Test
    fun `getActualStop should add parentheses to the end of the string value if required`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val elements = mock(Elements::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.actualStopSelector = "selector"

        given(htmlDocument.select(anyString())).willReturn(elements)
        given(elements.text()).willReturn("(STOP (VALUE))")
        given(stringCleaner.clean(anyString())).willReturn("cleaned")

        // WHEN
        val result = testSubject.getActualStop(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select(timetableSelectorProperties.actualStopSelector)
        verify(elements).text()
        verify(stringCleaner).clean("STOP (VALUE)")

        assertThat(result).isEqualTo("cleaned")
    }

    @Test
    fun `getActualStop should remove the dot from the end of the string if required`() {
        // GIVEN
        val htmlDocument = mock(Document::class.java)
        val elements = mock(Elements::class.java)
        val timetableSelectorProperties = TimetableSelectorProperties()
        timetableSelectorProperties.actualStopSelector = "selector"

        given(htmlDocument.select(anyString())).willReturn(elements)
        given(elements.text()).willReturn("(STOP.))")
        given(stringCleaner.clean(anyString())).willReturn("cleaned")

        // WHEN
        val result = testSubject.getActualStop(htmlDocument, timetableSelectorProperties)

        // THEN
        verify(htmlDocument).select(timetableSelectorProperties.actualStopSelector)
        verify(elements).text()
        verify(stringCleaner).clean("STOP")

        assertThat(result).isEqualTo("cleaned")
    }
}