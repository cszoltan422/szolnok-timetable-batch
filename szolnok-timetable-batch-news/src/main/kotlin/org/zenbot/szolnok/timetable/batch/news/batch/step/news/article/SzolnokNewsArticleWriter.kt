package org.zenbot.szolnok.timetable.batch.news.batch.step.news.article

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.domain.SzolnokNewsArticle

@Component
class SzolnokNewsArticleWriter : ItemWriter<SzolnokNewsArticle> {

    private val log = LoggerFactory.getLogger(SzolnokNewsArticleWriter::class.java)

    override fun write(elements: List<SzolnokNewsArticle>) {
        val szolnokNewsArticle = elements[0]
        log.info("Writing to database article=[{}]", szolnokNewsArticle)
    }
}