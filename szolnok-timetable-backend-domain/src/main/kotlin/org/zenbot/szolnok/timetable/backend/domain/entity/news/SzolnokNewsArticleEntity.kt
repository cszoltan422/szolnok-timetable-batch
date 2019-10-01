package org.zenbot.szolnok.timetable.backend.domain.entity.news

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "news_article")
data class SzolnokNewsArticleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var effectiveDate: String = "",
    var title: String = "",
    var detailed: Boolean = false,
    var content: String = ""
)