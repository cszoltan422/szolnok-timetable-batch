package org.zenbot.szolnok.timetable.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zenbot.szolnok.timetable.domain.BusStopWithBuses;

import java.util.Optional;

public interface BusStopRepository extends MongoRepository<BusStopWithBuses, String> {

    Optional<BusStopWithBuses> findByBusStopName(String s);
}
