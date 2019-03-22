package org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.safety.Whitelist
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.news.configuration.properties.NewsProperties
import org.zenbot.szolnok.timetable.backend.batch.news.configuration.properties.NewsSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

@Component
class NewsContentItemProcessorHelper(private val jsoupDocumentService: JsoupDocumentService) {

    fun getContent(element: Element, newsProperties: NewsProperties): String {
        val detailsButtons = element.select(newsProperties.selector.newsDetailsButtonSelector)
        if (detailsButtons.size > 0) {
            val link = detailsButtons[0].attr(newsProperties.selector.hrefSelector)

            val document = jsoupDocumentService.getDocument(newsProperties.resource.baseUrl + link)

            val elements = document.select(newsProperties.selector.newsDetailContentSelector)
            var innerHtml = elements.html()
            innerHtml = removeAllInlineStyles(innerHtml, newsProperties.selector)
            return innerHtml
        }
        return element.select(newsProperties.selector.newsContentSelector).text()
    }

    private fun removeAllInlineStyles(htmlString: String, selector: NewsSelectorProperties): String {
        val whitelistedTags = Whitelist.simpleText()
        whitelistedTags.addTags(selector.allowedTags)
        return Jsoup.clean(htmlString, whitelistedTags)
    }
}

private fun Whitelist.addTags(allowedTags: List<String>) {
    allowedTags.forEach { tag -> this.addTags(tag) }
}
