package org.zenbot.szolnok.timetable.batch.news.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("news")
data class NewsProperties(
    var selector: NewsSelectorProperties = NewsSelectorProperties(),
    var resource: NewsResourceProperties = NewsResourceProperties()
)