package org.zenbot.szolnok.timetable.batch.configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("org.zenbot.timetableupdater.dao")
public class MongoConfiguration {
}
