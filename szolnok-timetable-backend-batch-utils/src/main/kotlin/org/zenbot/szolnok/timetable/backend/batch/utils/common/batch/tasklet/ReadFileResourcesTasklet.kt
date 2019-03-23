package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage

@Component
@EnableConfigurationProperties(TimetableProperties::class)
class ReadFileResourcesTasklet(
    private val stringResourcesInMemoryStorage: StringResourcesInMemoryStorage,
    private val jsoupDocumentService: JsoupDocumentService,
    private val properties: TimetableProperties
) : Tasklet {

    private val log = LoggerFactory.getLogger(ReadFileResourcesTasklet::class.java)

    override fun execute(stepContribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("{}", properties.resource.selectedBuses)
        try {
            val landingPageHtml = jsoupDocumentService.getDocument(properties.resource.baseUrl + properties.resource.szolnokUrl)
            val busLinks = landingPageHtml.select(properties.selector.routesLinkSelector)
            busLinks.forEach { busLink -> saveBusStopUrlsOfBus(busLink, true) }
        } catch (e: IllegalStateException) {
            log.error("Could not resolve url=[{}]", properties.resource.baseUrl)
        }

        return RepeatStatus.FINISHED
    }

    private fun saveBusStopUrlsOfBus(busLink: Element, contenue: Boolean) {
        val busHtmlFile = getBusHtml(properties.resource.baseUrl, busLink)
        val routeName = busHtmlFile.select(properties.selector.routeNameSelector).text()
        if (properties.resource.selectedBuses.isEmpty() || properties.resource.selectedBuses.contains(routeName)) {
            log.info("Save urls for bus=[{}]", busHtmlFile.location())
            val busStops = busHtmlFile.select(properties.selector.stationsSelector)
            saveUrlResource(properties.resource.baseUrl, busStops)
            if (contenue) {
                val otherRoutesLink = busHtmlFile.select(properties.selector.otherRouteSelector)
                otherRoutesLink.forEach { otherRouteLink -> saveBusStopUrlsOfBus(otherRouteLink, false) }
            }
        }
    }

    private fun getBusHtml(baseUrl: String, busLink: Element): Document {
        val link = busLink.attr(properties.selector.hrefSelector)
        return jsoupDocumentService.getDocument(baseUrl + link)
    }

    private fun saveUrlResource(baseUrl: String, busStops: Elements) {
        busStops.forEach { busStop ->
            val busStopUrl = busStop.attr(properties.selector.hrefSelector)
            stringResourcesInMemoryStorage.addUrl(baseUrl + busStopUrl)
        }
    }
}
