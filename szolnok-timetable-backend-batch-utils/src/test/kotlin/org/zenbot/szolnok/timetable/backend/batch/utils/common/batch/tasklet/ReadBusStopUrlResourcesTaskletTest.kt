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
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.scope.context.StepContext
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableResourceProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage

class ReadBusStopUrlResourcesTaskletTest {

    private lateinit var testSubject: ReadBusStopUrlResourcesTasklet

    @Mock
    private lateinit var readBusStopUrlOfBusTaskletHelper: ReadBusStopUrlOfBusTaskletHelper

    @Mock
    private lateinit var stringResourcesInMemoryStorage: StringResourcesInMemoryStorage

    @Mock
    private lateinit var jsoupDocumentService: JsoupDocumentService

    private lateinit var properties: TimetableProperties

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        properties = TimetableProperties()
        val selectorPropeties = TimetableSelectorProperties()
        val resourcePropeties = TimetableResourceProperties()
        resourcePropeties.baseUrl = "baseUrl"
        resourcePropeties.szolnokUrl = "szolnokUrl"
        selectorPropeties.routesLinkSelector = "routesLinkSelector"
        properties.selector = selectorPropeties
        properties.resource = resourcePropeties

        testSubject = ReadBusStopUrlResourcesTasklet(
                stringResourcesInMemoryStorage,
                readBusStopUrlOfBusTaskletHelper,
                jsoupDocumentService,
                properties)
    }

    @Test
    fun `execute should catch the exception thrown by the documentService`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val jobParameters = mock(JobParameters::class.java)
        val stepContribution = mock(StepContribution::class.java)
        val chunkContext = mock(ChunkContext::class.java)
        val stepContext = mock(StepContext::class.java)
        val stepExecution = mock(StepExecution::class.java)

        given(chunkContext.stepContext).willReturn(stepContext)
        given(stepContext.stepExecution).willReturn(stepExecution)
        given(stepExecution.jobExecution).willReturn(jobExecution)
        given(jobExecution.jobParameters).willReturn(jobParameters)
        given(jobParameters.getString(anyString(), anyString())).willReturn("1A,2A")
        given(jsoupDocumentService.getDocument(anyString())).willThrow(IllegalStateException::class.java)

        // WHEN
        testSubject.execute(stepContribution, chunkContext)

        // THEN
        verify(stringResourcesInMemoryStorage).clear()
        verify(chunkContext).stepContext
        verify(stepContext).stepExecution
        verify(stepExecution).jobExecution
        verify(jobExecution).jobParameters
        verify(jobParameters).getString("selectedBuses", "")
        verify(jsoupDocumentService).getDocument("baseUrlszolnokUrl")
        Mockito.verifyNoMoreInteractions(readBusStopUrlOfBusTaskletHelper)
    }

    @Test
    fun `execute should fetch the landing page and save all the bus stops for each bus`() {
        // GIVEN
        val jobExecution = mock(JobExecution::class.java)
        val jobParameters = mock(JobParameters::class.java)
        val stepContribution = mock(StepContribution::class.java)
        val chunkContext = mock(ChunkContext::class.java)
        val stepContext = mock(StepContext::class.java)
        val stepExecution = mock(StepExecution::class.java)
        val landingPage = mock(Document::class.java)
        val busLinks = mock(Elements::class.java)
        val busLink1 = mock(Element::class.java)
        val busLink2 = mock(Element::class.java)
        val busLinksList = mutableListOf(busLink1, busLink2)

        given(chunkContext.stepContext).willReturn(stepContext)
        given(stepContext.stepExecution).willReturn(stepExecution)
        given(stepExecution.jobExecution).willReturn(jobExecution)
        given(jobExecution.jobParameters).willReturn(jobParameters)
        given(jobParameters.getString(anyString(), anyString())).willReturn("1A,2A")
        given(jsoupDocumentService.getDocument(anyString())).willReturn(landingPage)
        given(landingPage.select(anyString())).willReturn(busLinks)
        given(busLinks.iterator()).willReturn(busLinksList.iterator())

        // WHEN
        testSubject.execute(stepContribution, chunkContext)

        // THEN
        verify(stringResourcesInMemoryStorage).clear()
        verify(chunkContext).stepContext
        verify(stepContext).stepExecution
        verify(stepExecution).jobExecution
        verify(jobExecution).jobParameters
        verify(jobParameters).getString("selectedBuses", "")
        verify(jsoupDocumentService).getDocument("baseUrlszolnokUrl")
        verify(landingPage).select("routesLinkSelector")
        verify(busLinks).iterator()
        verify(readBusStopUrlOfBusTaskletHelper).saveBusStopUrlsOfBus(busLink1, "1A,2A")
        verify(readBusStopUrlOfBusTaskletHelper).saveBusStopUrlsOfBus(busLink2, "1A,2A")
    }
}
