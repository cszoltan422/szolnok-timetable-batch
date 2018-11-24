package org.zenbot.szolnok.timetable.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zenbot.szolnok.timetable.domain.Bus;

public interface BusRepository extends MongoRepository<Bus, String> {

    Bus findByBusName(String busName);
}
