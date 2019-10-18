package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableResourceProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

class ReadBusStopUrlResourcesTaskletTest {

    private lateinit var testSubject: ReadBusStopUrlResourcesTasklet

    @Mock
    private lateinit var readBusStopUrlOfBusTaskletHelper: ReadBusStopUrlOfBusTaskletHelper

    @Mock
    private lateinit var jsoupDocumentService: JsoupDocumentService

    private lateinit var properties: TimetableProperties

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        properties = TimetableProperties()
        val selectorPropeties = TimetableSelectorProperties()
        val resourcePropeties = TimetableResourceProperties()
        resourcePropeties.selectedBuses = listOf("1A", "2A")
        resourcePropeties.baseUrl = "baseUrl"
        resourcePropeties.szolnokUrl = "szolnokUrl"
        selectorPropeties.routesLinkSelector = "routesLinkSelector"
        properties.selector = selectorPropeties
        properties.resource = resourcePropeties

        testSubject = ReadBusStopUrlResourcesTasklet(
                readBusStopUrlOfBusTaskletHelper,
                jsoupDocumentService,
                properties)
    }

    @Test
    fun `execute should catch the exception thrown by the documentService`() {
        // GIVEN
        val stepContribution = mock(StepContribution::class.java)
        val chunkContext = mock(ChunkContext::class.java)

        given(jsoupDocumentService.getDocument(anyString())).willThrow(IllegalStateException::class.java)

        // WHEN
        testSubject.execute(stepContribution, chunkContext)

        // THEN
        verify(jsoupDocumentService).getDocument("baseUrlszolnokUrl")
        Mockito.verifyNoMoreInteractions(readBusStopUrlOfBusTaskletHelper)
    }

    @Test
    fun `execute should fetch the landing page and save all the bus stops for each bus`() {
        // GIVEN
        val stepContribution = mock(StepContribution::class.java)
        val chunkContext = mock(ChunkContext::class.java)
        val landingPage = mock(Document::class.java)
        val busLinks = mock(Elements::class.java)
        val busLink1 = mock(Element::class.java)
        val busLink2 = mock(Element::class.java)
        val busLinksList = mutableListOf(busLink1, busLink2)

        given(jsoupDocumentService.getDocument(anyString())).willReturn(landingPage)
        given(landingPage.select(anyString())).willReturn(busLinks)
        given(busLinks.iterator()).willReturn(busLinksList.iterator())

        // WHEN
        testSubject.execute(stepContribution, chunkContext)

        // THEN
        verify(jsoupDocumentService).getDocument("baseUrlszolnokUrl")
        verify(landingPage).select("routesLinkSelector")
        verify(busLinks).iterator()
        verify(readBusStopUrlOfBusTaskletHelper).saveBusStopUrlsOfBus(busLink1)
        verify(readBusStopUrlOfBusTaskletHelper).saveBusStopUrlsOfBus(busLink2)
    }
}
