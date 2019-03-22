package org.zenbot.szolnok.timetable.backend.batch.news.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("news")
data class NewsProperties(
    var selector: NewsSelectorProperties = NewsSelectorProperties(),
    var resource: NewsResourceProperties = NewsResourceProperties()
)