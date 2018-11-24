package org.zenbot.szolnok.timetable.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("org.zenbot.szolnok.timetable.dao")
public class MongoConfiguration {
}
