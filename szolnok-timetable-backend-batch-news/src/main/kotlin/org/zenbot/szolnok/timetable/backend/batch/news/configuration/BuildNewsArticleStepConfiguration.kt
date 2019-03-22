package org.zenbot.szolnok.timetable.backend.batch.news.configuration

import org.jsoup.nodes.Element
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article.JsoupElementReader
import org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article.SzolnokNewsArticelItemProcessor
import org.zenbot.szolnok.timetable.backend.batch.news.batch.step.news.article.SzolnokNewsArticleWriter
import org.zenbot.szolnok.timetable.backend.batch.news.domain.SzolnokNewsArticle

@Configuration
class BuildNewsArticleStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val jsoupElementReader: JsoupElementReader,
    private val szolnokNewsArticelItemProcessor: SzolnokNewsArticelItemProcessor,
    private val szolnokNewsArticleWriter: SzolnokNewsArticleWriter
) {

    @Bean
    fun buildNewsArticleStep(): Step {
        return stepBuilderFactory.get("buildNewsArticleStep")
                .chunk<Element, SzolnokNewsArticle>(1)
                .reader(jsoupElementReader)
                .processor(szolnokNewsArticelItemProcessor)
                .writer(szolnokNewsArticleWriter)
                .build()
    }
}