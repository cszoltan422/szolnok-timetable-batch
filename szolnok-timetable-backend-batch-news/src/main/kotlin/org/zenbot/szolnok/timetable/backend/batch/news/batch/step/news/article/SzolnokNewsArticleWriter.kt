package org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.zenbot.szolnok.timetable.backend.domain.entity.news.SzolnokNewsArticleEntity
import org.zenbot.szolnok.timetable.backend.repository.NewsArticleRepository

@Component
@Transactional
class SzolnokNewsArticleWriter(private val newsArticleRepository: NewsArticleRepository) : ItemWriter<SzolnokNewsArticleEntity> {

    private val log = LoggerFactory.getLogger(SzolnokNewsArticleWriter::class.java)

    override fun write(elements: List<SzolnokNewsArticleEntity>) {
        if (elements.size > 1) {
            throw IllegalArgumentException("Size of the list should be [1]! Actual size is [${elements.size}]")
        }

        val szolnokNewsArticle = elements[0]
        log.info("Writing to database article=[{}]", szolnokNewsArticle)
        newsArticleRepository.save(szolnokNewsArticle)
    }
}