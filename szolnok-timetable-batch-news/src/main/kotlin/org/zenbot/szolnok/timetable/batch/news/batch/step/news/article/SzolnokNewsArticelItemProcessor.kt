package org.zenbot.szolnok.timetable.batch.news.batch.step.news.article

import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.configuration.properties.NewsProperties
import org.zenbot.szolnok.timetable.batch.news.domain.SzolnokNewsArticle

@Component
class SzolnokNewsArticelItemProcessor (
        private val newsProperties: NewsProperties,
        private val newsContentItemProcessorHelper: NewsContentItemProcessorHelper) : ItemProcessor<Element, SzolnokNewsArticle> {

    private val log = LoggerFactory.getLogger(SzolnokNewsArticelItemProcessor::class.java)

    override fun process(element: Element): SzolnokNewsArticle {
        val effectiveDate = element.select(newsProperties.selector.newsDateSelector).text()
        val title = element.select(newsProperties.selector.newsTitleSelector).text()
        val content = newsContentItemProcessorHelper.getContent(element, newsProperties)
        val detailed = element.select(newsProperties.selector.newsDetailsButtonSelector).size > 0
        log.info("Processing elememt with effectiveDate=[{}]", effectiveDate)

        val result = SzolnokNewsArticle()
        result.effectiveDate = effectiveDate
        result.title = title
        result.content = content
        result.detailed = detailed
        return result
    }
}