package org.zenbot.szolnok.timetable.batch.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zenbot.szolnok.timetable.batch.domain.Bus;

public interface BusRepository extends MongoRepository<Bus, String> {

    Bus findByBusName(String busName);
}
