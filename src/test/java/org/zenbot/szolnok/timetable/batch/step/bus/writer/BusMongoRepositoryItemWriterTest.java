package org.zenbot.szolnok.timetable.batch.step.bus.writer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zenbot.szolnok.timetable.dao.BusRepository;
import org.zenbot.szolnok.timetable.domain.Bus;
import org.zenbot.szolnok.timetable.domain.BusRoute;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

public class BusMongoRepositoryItemWriterTest {

    private static final String BUS_NAME = "BUS";
    private static final String END_BUS_STOP = "END_STOP";
    private static final String START_BUS_STOP = "START_STOP";

    private BusMongoRepositoryItemWriter testSubject;

    @Mock
    private BusRepository busRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        testSubject = new BusMongoRepositoryItemWriter(busRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeWithMoreThanTwoElementsShouldThrowException() {
        // GIVEN
        List<Bus> buses = new ArrayList<>();
        buses.add(new Bus());
        buses.add(new Bus());

        // WHEN
        testSubject.write(buses);

        //THEN exception is thrown...
    }

    @Test
    public void writeWithOneParameterShouldCallTheRepositoryWithFirstElement() {
        // GIVEN
        List<Bus> buses = new ArrayList<>();
        Bus bus = new Bus();
        bus.setBusName(BUS_NAME);
        List<BusRoute> busRoutes = new ArrayList<>();
        BusRoute busRoute = new BusRoute();
        busRoute.setStartBusStop(START_BUS_STOP);
        busRoute.setEndBusStop(END_BUS_STOP);
        busRoutes.add(busRoute);
        bus.setBusRoutes(busRoutes);
        buses.add(bus);

        // WHEN
        testSubject.write(buses);

        //THEN
        verify(busRepository).save(same(bus));
    }
}