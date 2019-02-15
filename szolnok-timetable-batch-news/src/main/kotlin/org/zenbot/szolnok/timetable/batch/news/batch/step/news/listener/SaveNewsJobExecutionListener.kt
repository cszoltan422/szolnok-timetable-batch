package org.zenbot.szolnok.timetable.batch.news.batch.step.news.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.dao.NewsArticleRepository

@Component
class SaveNewsJobExecutionListener(private val newsArticleRepository: NewsArticleRepository) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(SaveNewsJobExecutionListener::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Removing all articles from database!")
        newsArticleRepository.deleteAll()
    }

    override fun afterJob(jobExecution: JobExecution) { }
}