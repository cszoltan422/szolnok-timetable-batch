package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.BusNameSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableResourceProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage

class ReadBusStopUrlOfBusTaskletHelperTest {

    private lateinit var testSubject: ReadBusStopUrlOfBusTaskletHelper

    @Mock
    private lateinit var busNameSelectorItemProcessorHelper: BusNameSelectorItemProcessorHelper

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
        selectorPropeties.hrefSelector = "hrefSelector"
        selectorPropeties.stationsSelector = "stationsSelector"
        selectorPropeties.otherRouteSelector = "otherRouteSelector"
        resourcePropeties.baseUrl = "baseUrl"
        properties.selector = selectorPropeties
        properties.resource = resourcePropeties

        testSubject = ReadBusStopUrlOfBusTaskletHelper(
                busNameSelectorItemProcessorHelper,
                stringResourcesInMemoryStorage,
                jsoupDocumentService,
                properties)
    }

    @Test
    fun `saveBusStopUrlsOfBus should not save the bus stop links if selected buses not contain the current bus`() {
        // GIVEN
        properties.resource.selectedBuses = listOf("1")

        val busLink = mock(Element::class.java)
        val busHtml = mock(Document::class.java)

        given(busLink.attr(anyString())).willReturn("link")
        given(jsoupDocumentService.getDocument(anyString())).willReturn(busHtml)
        given(busNameSelectorItemProcessorHelper.getBusName(busHtml, properties.selector)).willReturn("2")

        // WHEN
        testSubject.saveBusStopUrlsOfBus(busLink)

        // THEN
        verify(busLink).attr("hrefSelector")
        verify(jsoupDocumentService).getDocument("baseUrllink")
        verify(busNameSelectorItemProcessorHelper).getBusName(busHtml, properties.selector)
        verify(busHtml, never()).select(anyString())
        verify(stringResourcesInMemoryStorage, never()).addUrl(anyString())
    }

    @Test
    fun `saveBusStopUrlsOfBus save the bus stop links if selected buses are empty`() {
        // GIVEN
        properties.resource.selectedBuses = listOf()

        val busLink = mock(Element::class.java)
        val busHtml = mock(Document::class.java)
        val busStopLinks = mock(Elements::class.java)
        val busStopLink1 = mock(Element::class.java)
        val busStopLink2 = mock(Element::class.java)
        val busStopLinksList = mutableListOf(busStopLink1, busStopLink2)
        val otherRoutes = mock(Elements::class.java)
        val otherRoute = mock(Element::class.java)
        val otherRoutesList = mutableListOf(otherRoute)
        val otherRouteDocument = mock(Document::class.java)
        val otherRouteStops = mock(Elements::class.java)
        val otherRouteStop = mock(Element::class.java)
        val otherRouteStopsList = mutableListOf(otherRouteStop)

        given(busLink.attr(anyString())).willReturn("link")
        given(jsoupDocumentService.getDocument("baseUrllink")).willReturn(busHtml)
        given(busNameSelectorItemProcessorHelper.getBusName(busHtml, properties.selector)).willReturn("2")
        given(busHtml.select("stationsSelector")).willReturn(busStopLinks)
        given(busStopLinks.iterator()).willReturn(busStopLinksList.iterator())
        given(busStopLink1.attr(anyString())).willReturn("busStopLink1")
        given(busStopLink2.attr(anyString())).willReturn("busStopLink2")
        given(busHtml.select("otherRouteSelector")).willReturn(otherRoutes)
        given(otherRoutes.iterator()).willReturn(otherRoutesList.iterator())
        given(otherRoute.attr(anyString())).willReturn("otherRouteLink")
        given(jsoupDocumentService.getDocument("baseUrlotherRouteLink")).willReturn(otherRouteDocument)
        given(otherRouteDocument.select("stationsSelector")).willReturn(otherRouteStops)
        given(otherRouteStops.iterator()).willReturn(otherRouteStopsList.iterator())
        given(otherRouteStop.attr(anyString())).willReturn("otherRouteStopLink")

        // WHEN
        testSubject.saveBusStopUrlsOfBus(busLink)

        // THEN
        verify(busLink).attr("hrefSelector")
        verify(jsoupDocumentService).getDocument("baseUrllink")
        verify(busNameSelectorItemProcessorHelper).getBusName(busHtml, properties.selector)
        verify(busHtml).select("stationsSelector")
        verify(busStopLinks).iterator()
        verify(busStopLink1).attr("hrefSelector")
        verify(busStopLink2).attr("hrefSelector")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlbusStopLink1")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlbusStopLink2")
        verify(busHtml).select("otherRouteSelector")
        verify(otherRoutes).iterator()
        verify(otherRoute).attr("hrefSelector")
        verify(jsoupDocumentService).getDocument("baseUrlotherRouteLink")
        verify(otherRouteDocument).select("stationsSelector")
        verify(otherRouteStops).iterator()
        verify(otherRouteStop).attr("hrefSelector")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlotherRouteStopLink")
    }

    @Test
    fun `saveBusStopUrlsOfBus save the bus stop links if selected buses contains the given bus`() {
        // GIVEN
        properties.resource.selectedBuses = listOf("2")

        val busLink = mock(Element::class.java)
        val busHtml = mock(Document::class.java)
        val busStopLinks = mock(Elements::class.java)
        val busStopLink1 = mock(Element::class.java)
        val busStopLink2 = mock(Element::class.java)
        val busStopLinksList = mutableListOf(busStopLink1, busStopLink2)
        val otherRoutes = mock(Elements::class.java)
        val otherRoute = mock(Element::class.java)
        val otherRoutesList = mutableListOf(otherRoute)
        val otherRouteDocument = mock(Document::class.java)
        val otherRouteStops = mock(Elements::class.java)
        val otherRouteStop = mock(Element::class.java)
        val otherRouteStopsList = mutableListOf(otherRouteStop)

        given(busLink.attr(anyString())).willReturn("link")
        given(jsoupDocumentService.getDocument("baseUrllink")).willReturn(busHtml)
        given(busNameSelectorItemProcessorHelper.getBusName(busHtml, properties.selector)).willReturn("2")
        given(busHtml.select("stationsSelector")).willReturn(busStopLinks)
        given(busStopLinks.iterator()).willReturn(busStopLinksList.iterator())
        given(busStopLink1.attr(anyString())).willReturn("busStopLink1")
        given(busStopLink2.attr(anyString())).willReturn("busStopLink2")
        given(busHtml.select("otherRouteSelector")).willReturn(otherRoutes)
        given(otherRoutes.iterator()).willReturn(otherRoutesList.iterator())
        given(otherRoute.attr(anyString())).willReturn("otherRouteLink")
        given(jsoupDocumentService.getDocument("baseUrlotherRouteLink")).willReturn(otherRouteDocument)
        given(otherRouteDocument.select("stationsSelector")).willReturn(otherRouteStops)
        given(otherRouteStops.iterator()).willReturn(otherRouteStopsList.iterator())
        given(otherRouteStop.attr(anyString())).willReturn("otherRouteStopLink")

        // WHEN
        testSubject.saveBusStopUrlsOfBus(busLink)

        // THEN
        verify(busLink).attr("hrefSelector")
        verify(jsoupDocumentService).getDocument("baseUrllink")
        verify(busNameSelectorItemProcessorHelper).getBusName(busHtml, properties.selector)
        verify(busHtml).select("stationsSelector")
        verify(busStopLinks).iterator()
        verify(busStopLink1).attr("hrefSelector")
        verify(busStopLink2).attr("hrefSelector")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlbusStopLink1")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlbusStopLink2")
        verify(busHtml).select("otherRouteSelector")
        verify(otherRoutes).iterator()
        verify(otherRoute).attr("hrefSelector")
        verify(jsoupDocumentService).getDocument("baseUrlotherRouteLink")
        verify(otherRouteDocument).select("stationsSelector")
        verify(otherRouteStops).iterator()
        verify(otherRouteStop).attr("hrefSelector")
        verify(stringResourcesInMemoryStorage).addUrl("baseUrlotherRouteStopLink")
    }
}
