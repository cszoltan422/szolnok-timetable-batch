package org.zenbot.szolnok.timetable.batch.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zenbot.szolnok.timetable.batch.domain.BusStop;
import org.zenbot.szolnok.timetable.batch.domain.BusStopWithRoutes;

import java.util.Optional;

public interface BusStopRepository extends MongoRepository<BusStopWithRoutes, String> {

    Optional<BusStopWithRoutes> findByBusStopName(String s);
}
