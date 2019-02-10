package org.zenbot.szolnok.timetable.batch.news.configuration.properties

data class NewsSelectorProperties(
    var newsElementsSelector: String = "",
    var newsTitleSelector: String = "",
    var newsDateSelector: String = "",
    var newsContentSelector: String = ""
)