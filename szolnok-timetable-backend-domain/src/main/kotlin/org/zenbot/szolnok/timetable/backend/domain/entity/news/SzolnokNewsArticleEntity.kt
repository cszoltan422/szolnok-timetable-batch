package org.zenbot.szolnok.timetable.backend.domain.entity.news

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(schema = "szolnok_app", name = "news_article")
data class SzolnokNewsArticleEntity(

    @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    var id: Long? = null,

    @Column(name = "effective_date")
    var effectiveDate: String = "",

    @Column(name = "title")
    var title: String = "",

    @Column(name = "detailed")
    var detailed: Boolean = false,

    @Lob
    @Column(name = "content")
    var content: String = ""
)