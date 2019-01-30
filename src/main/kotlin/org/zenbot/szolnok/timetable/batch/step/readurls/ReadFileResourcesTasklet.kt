package org.zenbot.szolnok.timetable.batch.step.readurls

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
import org.zenbot.szolnok.timetable.batch.step.bus.processor.JsoupDocumentService
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties
import org.zenbot.szolnok.timetable.configuration.properties.TimetableResourceProperties
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties

@Component
@EnableConfigurationProperties(TimetableProperties::class)
class ReadFileResourcesTasklet(
    private val stringResourcesInMemoryStorage: StringResourcesInMemoryStorage,
    private val jsoupDocumentService: JsoupDocumentService,
    properties: TimetableProperties
) : Tasklet {

    private val log = LoggerFactory.getLogger(ReadFileResourcesTasklet::class.java)

    private val resourceProperties: TimetableResourceProperties
    private val selectorProperties: TimetableSelectorProperties

    init {
        this.resourceProperties = properties.resource
        this.selectorProperties = properties.selector
    }

    override fun execute(stepContribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("{}", resourceProperties.selectedBuses)
        try {
            val landingPageHtml = jsoupDocumentService.getDocument(resourceProperties.baseUrl + resourceProperties.szolnokUrl)
            val busLinks = landingPageHtml.select(selectorProperties.routesLinkSelector)
            busLinks.forEach { busLink -> saveBusStopUrlsOfBus(busLink, true) }
        } catch (e: IllegalStateException) {
            log.error("Could not resolve url=[{}]", resourceProperties.baseUrl)
        }

        return RepeatStatus.FINISHED
    }

    private fun saveBusStopUrlsOfBus(busLink: Element, contenue: Boolean) {
        val busHtmlFile = getBusHtml(resourceProperties.baseUrl, busLink)
        val routeName = busHtmlFile.select(selectorProperties.routeNameSelector).text()
        if (resourceProperties.selectedBuses.isEmpty() || resourceProperties.selectedBuses.contains(routeName)) {
            log.info("Save urls for bus=[{}]", busHtmlFile.location())
            val busStops = busHtmlFile.select(selectorProperties.stationsSelector)
            saveUrlResource(resourceProperties.baseUrl, busStops)
            if (contenue) {
                val otherRoutesLink = busHtmlFile.select(selectorProperties.otherRouteSelector)
                otherRoutesLink.forEach { otherRouteLink -> saveBusStopUrlsOfBus(otherRouteLink, false) }
            }
        }
    }

    private fun getBusHtml(baseUrl: String, busLink: Element): Document {
        val link = busLink.attr(selectorProperties.hrefSelector)
        return jsoupDocumentService.getDocument(baseUrl + link)
    }

    private fun saveUrlResource(baseUrl: String, busStops: Elements) {
        busStops.forEach { busStop ->
            val busStopUrl = busStop.attr(selectorProperties.hrefSelector)
            stringResourcesInMemoryStorage.addUrl(baseUrl + busStopUrl)
        }
    }
}
