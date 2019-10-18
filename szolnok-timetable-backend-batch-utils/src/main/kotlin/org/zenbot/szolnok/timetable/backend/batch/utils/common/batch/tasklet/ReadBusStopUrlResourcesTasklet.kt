package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.tasklet

import org.jsoup.nodes.Document
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

/**
 * Reads all the bus stop's URL into an inmemory storage for later processing
 */
@Component
@EnableConfigurationProperties(TimetableProperties::class)
class ReadBusStopUrlResourcesTasklet(
    private val readBusStopUrlOfBusTaskletHelper: ReadBusStopUrlOfBusTaskletHelper,
    private val jsoupDocumentService: JsoupDocumentService,
    private val properties: TimetableProperties
) : Tasklet {

    private val log = LoggerFactory.getLogger(ReadBusStopUrlResourcesTasklet::class.java)

    override fun execute(stepContribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("{}", properties.resource.selectedBuses)
        try {
            val landingPageHtml = fetchLandingPage()
            val busLinks = selectBusesLinks(landingPageHtml)
            saveAllBusStopsForBus(busLinks)
        } catch (e: IllegalStateException) {
            log.error("Could not resolve url=[{}]", properties.resource.baseUrl)
        }

        return RepeatStatus.FINISHED
    }

    private fun saveAllBusStopsForBus(busLinks: Elements) {
        busLinks.forEach { busLink -> readBusStopUrlOfBusTaskletHelper.saveBusStopUrlsOfBus(busLink) }
    }

    private fun selectBusesLinks(landingPageHtml: Document) =
        landingPageHtml.select(properties.selector.routesLinkSelector)

    private fun fetchLandingPage(): Document {
        val url = properties.resource.baseUrl + properties.resource.szolnokUrl
        val landingPageHtml = jsoupDocumentService.getDocument(url)
        return landingPageHtml
    }
}
