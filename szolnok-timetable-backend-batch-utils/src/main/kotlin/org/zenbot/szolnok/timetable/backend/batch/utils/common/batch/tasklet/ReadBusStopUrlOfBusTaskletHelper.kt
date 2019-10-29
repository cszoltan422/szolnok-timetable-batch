package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.BusNameSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage

/**
 * Reads all the urls for a bus represented by hyperlink and saves into [StringResourcesInMemoryStorage]
 */
@Component
class ReadBusStopUrlOfBusTaskletHelper(
    private val busNameSelectorItemProcessorHelper: BusNameSelectorItemProcessorHelper,
    private val stringResourcesInMemoryStorage: StringResourcesInMemoryStorage,
    private val jsoupDocumentService: JsoupDocumentService,
    private val properties: TimetableProperties
) {

    private val log = LoggerFactory.getLogger(ReadBusStopUrlResourcesTasklet::class.java)

    /**
     * Saves all the busStop urls of a bus represented by hyperlink and saves into [StringResourcesInMemoryStorage]
     * @param busLink The link of the bus ta save it's every busStop
     * @param selectedBuses List of buses to process in the current context. Strings delimited by a comma: ,
     */
    fun saveBusStopUrlsOfBus(busLink: Element, selectedBuses: String) {
        val busHtmlFile = fetchHtmlForLink(busLink)
        val routeName = busNameSelectorItemProcessorHelper.getBusName(busHtmlFile, properties.selector)
        if (selectedBuses.isEmpty() || shouldProcessBus(routeName, selectedBuses.split(","))) {
            log.info("Save urls for bus=[{}]", busHtmlFile.location())
            saveBusStopUrls(busHtmlFile)
            val otherRoutesLink = selectOtherRoute(busHtmlFile)
            otherRoutesLink.forEach { otherRoute -> saveBusStopUrls(fetchHtmlForLink(otherRoute)) }
        }
    }

    private fun fetchHtmlForLink(busLink: Element): Document {
        val link = busLink.attr(properties.selector.hrefSelector)
        return jsoupDocumentService.getDocument(properties.resource.baseUrl + link)
    }

    private fun saveBusStopUrls(busHtmlFile: Document) {
        val busStops = selectBusStopsLinks(busHtmlFile)
        saveUrls(busStops)
    }

    private fun saveUrls(busStops: Elements) {
        busStops.forEach { busStop ->
            val busStopUrl = busStop.attr(properties.selector.hrefSelector)
            stringResourcesInMemoryStorage.addUrl(properties.resource.baseUrl + busStopUrl)
        }
    }

    private fun selectBusStopsLinks(busHtmlFile: Document) =
            busHtmlFile.select(properties.selector.stationsSelector)

    private fun selectOtherRoute(busHtmlFile: Document) =
            busHtmlFile.select(properties.selector.otherRouteSelector)

    private fun shouldProcessBus(routeName: String, selectedBuses: List<String>) =
            selectedBuses.isEmpty() || selectedBuses.contains(routeName)
}
