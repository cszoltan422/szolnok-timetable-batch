package org.zenbot.szolnok.timetable.batch.stops.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories("org.zenbot.szolnok.timetable.batch.stops.dao")
class MongoConfiguration