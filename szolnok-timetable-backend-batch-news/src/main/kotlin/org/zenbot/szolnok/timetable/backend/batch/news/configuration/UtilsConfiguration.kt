package org.zenbot.szolnok.timetable.backend.batch.news.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

@Configuration
class UtilsConfiguration {

    @Bean
    fun jsoupDocumentService(): JsoupDocumentService {
        return JsoupDocumentService()
    }
}