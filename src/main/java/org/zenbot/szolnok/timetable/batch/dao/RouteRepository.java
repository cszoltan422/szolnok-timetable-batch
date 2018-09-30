package org.zenbot.szolnok.timetable.batch.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zenbot.szolnok.timetable.batch.domain.BusRoute;

public interface RouteRepository extends MongoRepository<BusRoute, String> {

    BusRoute findByRoutename(String routename);
}
