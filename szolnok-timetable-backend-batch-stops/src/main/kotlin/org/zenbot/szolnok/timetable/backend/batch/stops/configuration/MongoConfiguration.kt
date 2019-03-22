package org.zenbot.szolnok.timetable.backend.batch.stops.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories("org.zenbot.szolnok.timetable.backend.batch.stops.dao")
class MongoConfiguration