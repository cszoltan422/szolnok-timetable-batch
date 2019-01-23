package org.zenbot.szolnok.timetable.batch.listener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobExecution;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableResourceProperties;
import org.zenbot.szolnok.timetable.dao.BusRepository;
import org.zenbot.szolnok.timetable.dao.BusStopRepository;
import org.zenbot.szolnok.timetable.domain.Bus;
import org.zenbot.szolnok.timetable.domain.BusRoute;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class RemoveBusRoutesExecutionListenerTest {

    private static final String BUS_1 = "BUS_1";
    private static final String BUS_2 = "BUS_2";
    private RemoveBusRoutesExecutionListener testSubject;

    @Mock
    private BusStopRepository busStopRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private TimetableResourceProperties properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testSubject = new RemoveBusRoutesExecutionListener(busRepository, busStopRepository, properties);
    }

    @Test
    public void beforeJobWithSelectedBusesShouldOnlyRemoveRoutesForThem() {
        // GIVEN
        List<String> selectedBuses = new ArrayList<>();
        selectedBuses.add(BUS_1);
        given(properties.getSelectedBuses()).willReturn(selectedBuses);

        List<Bus> buses = new ArrayList<>();
        Bus bus1 = getBus(BUS_1);
        buses.add(bus1);

        Bus bus2 = getBus(BUS_2);
        buses.add(bus2);

        given(busRepository.findAll()).willReturn(buses);

        // WHEN
        testSubject.beforeJob(new JobExecution(0L));

        //THEN
        verify(busRepository).findAll();
        verify(busRepository).saveAll(same(buses));
        verify(busStopRepository, never()).deleteAll();

        assertThat(bus1.getBusRoutes()).isEmpty();
        assertThat(bus2.getBusRoutes()).isNotEmpty();
    }

    @Test
    public void beforeJObWithNoSelectedBusesShouldRemoveAll() {
        // GIVEN
        given(properties.getSelectedBuses()).willReturn(new ArrayList<>());

        List<Bus> buses = new ArrayList<>();
        Bus bus1 = getBus(BUS_1);
        buses.add(bus1);

        Bus bus2 = getBus(BUS_2);
        buses.add(bus2);

        given(busRepository.findAll()).willReturn(buses);

        // WHEN
        testSubject.beforeJob(new JobExecution(0L));

        //THEN
        verify(busRepository).findAll();
        verify(busRepository).saveAll(same(buses));
        verify(busStopRepository).deleteAll();

        assertThat(bus1.getBusRoutes()).isEmpty();
        assertThat(bus2.getBusRoutes()).isEmpty();
    }

    private Bus getBus(String busName) {
        Bus bus = new Bus();
        bus.setBusName(busName);
        ArrayList<BusRoute> busRoutes = new ArrayList<>();
        busRoutes.add(new BusRoute());
        bus.setBusRoutes(busRoutes);
        return bus;
    }
}