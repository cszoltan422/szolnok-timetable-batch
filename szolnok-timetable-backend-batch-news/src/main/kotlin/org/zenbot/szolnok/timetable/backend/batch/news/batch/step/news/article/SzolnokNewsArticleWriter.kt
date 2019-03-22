package org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.news.dao.NewsArticleRepository
import org.zenbot.szolnok.timetable.backend.domain.document.news.SzolnokNewsArticle

@Component
class SzolnokNewsArticleWriter(private val newsArticleRepository: NewsArticleRepository) : ItemWriter<SzolnokNewsArticle> {

    private val log = LoggerFactory.getLogger(SzolnokNewsArticleWriter::class.java)

    override fun write(elements: List<SzolnokNewsArticle>) {
        if (elements.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [${elements.size}]")
        }

        val szolnokNewsArticle = elements[0]
        log.info("Writing to database article=[{}]", szolnokNewsArticle)
        newsArticleRepository.save(szolnokNewsArticle)
    }
}