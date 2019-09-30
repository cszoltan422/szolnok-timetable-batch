package org.zenbot.szolnok.timetable.backend.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.zenbot.szolnok.timetable.backend.domain.entity.news.SzolnokNewsArticleEntity

@Repository
interface NewsArticleRepository : CrudRepository<SzolnokNewsArticleEntity, Long>