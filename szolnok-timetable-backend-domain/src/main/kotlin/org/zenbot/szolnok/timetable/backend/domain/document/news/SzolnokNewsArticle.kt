package org.zenbot.szolnok.timetable.backend.domain.document.news

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class SzolnokNewsArticle(
    @Id var id: String? = null,
    var effectiveDate: String = "",
    var title: String = "",
    var detailed: Boolean = false,
    var content: String = ""
)