package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.backend.domain.document.news.SzolnokNewsArticle

interface NewsArticleRepository : MongoRepository<SzolnokNewsArticle, String>