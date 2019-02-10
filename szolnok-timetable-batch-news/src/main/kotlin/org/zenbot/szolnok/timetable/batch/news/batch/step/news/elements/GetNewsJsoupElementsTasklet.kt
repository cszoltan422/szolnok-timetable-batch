package org.zenbot.szolnok.timetable.batch.news.batch.step.news.elements

import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.configuration.properties.NewsProperties
import org.zenbot.szolnok.timetable.batch.utils.common.service.JsoupDocumentService

@Component
@EnableConfigurationProperties(NewsProperties::class)
class GetNewsJsoupElementsTasklet(
    private val jsoupDocumentService: JsoupDocumentService,
    private val newsProperties: NewsProperties,
    private val jsoupElementsInMemoryStorage: JsoupElementsInMemoryStorage
) : Tasklet {

    private val log = LoggerFactory.getLogger(GetNewsJsoupElementsTasklet::class.java)

    override fun execute(stepContribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("Getting all news jsoup elements")
        val document = jsoupDocumentService.getDocument(newsProperties.resource.baseUrl)
        val elements = document.select(newsProperties.selector.newsElementsSelector)
        elements.forEach { element ->
            val titleElements = element.select(newsProperties.selector.newsTitleSelector).text()
            if (titleElements.equals("SZOLNOK")) {
                jsoupElementsInMemoryStorage.addElement(element)
            }
        }
        return RepeatStatus.FINISHED
    }
}