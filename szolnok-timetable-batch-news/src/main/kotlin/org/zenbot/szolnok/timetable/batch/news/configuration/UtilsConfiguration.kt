package org.zenbot.szolnok.timetable.batch.news.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.utils.common.service.JsoupDocumentService

@Configuration
class UtilsConfiguration {

    @Bean
    fun jsoupDocumentService(): JsoupDocumentService {
        return JsoupDocumentService()
    }
}