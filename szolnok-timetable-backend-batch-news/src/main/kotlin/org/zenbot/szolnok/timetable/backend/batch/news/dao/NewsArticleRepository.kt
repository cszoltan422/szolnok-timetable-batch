package org.zenbot.szolnok.timetable.backend.batch.news.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.zenbot.szolnok.timetable.backend.batch.news.domain.SzolnokNewsArticle

interface NewsArticleRepository : MongoRepository<SzolnokNewsArticle, String>