package org.zenbot.szolnok.timetable.batch.news.batch.step.news.article

import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.configuration.properties.NewsProperties
import org.zenbot.szolnok.timetable.batch.news.domain.SzolnokNewsArticle

@Component
class SzolnokNewsArticelItemProcessor(private val newsProperties: NewsProperties) : ItemProcessor<Element, SzolnokNewsArticle> {

    private val log = LoggerFactory.getLogger(SzolnokNewsArticelItemProcessor::class.java)

    override fun process(element: Element): SzolnokNewsArticle {
        val effectiveDate = element.select(newsProperties.selector.newsDateSelector).text()
        val content = element.select(newsProperties.selector.newsContentSelector).text()
        log.info("Processing elememt with effectiveDate=[{}]", effectiveDate)
        val result = SzolnokNewsArticle()
        result.effectiveDate = effectiveDate
        result.content = content
        return result
    }
}