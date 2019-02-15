package org.zenbot.szolnok.timetable.batch.news.configuration.properties

data class NewsSelectorProperties(
    var newsElementsSelector: String = "",
    var newsCitySelector: String = "",
    var newsDateSelector: String = "",
    var newsTitleSelector: String = "",
    var newsContentSelector: String = "",
    var newsDetailsButtonSelector: String = "",
    var hrefSelector: String = "",
    var newsDetailContentSelector: String = "",
    var allowedTags: List<String> = ArrayList()
)