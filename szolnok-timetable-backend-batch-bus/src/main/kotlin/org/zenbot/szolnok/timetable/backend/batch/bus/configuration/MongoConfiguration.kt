package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories("org.zenbot.szolnok.timetable.backend.repository")
class MongoConfiguration
