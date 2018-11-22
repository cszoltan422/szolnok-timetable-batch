package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.domain.Bus;
import org.zenbot.szolnok.timetable.batch.domain.BusRoute;
import org.zenbot.szolnok.timetable.batch.domain.BusStop;
import org.zenbot.szolnok.timetable.batch.domain.Timetable;

@Slf4j
@Component
public class TimetableToBusItemProcessor implements ItemProcessor<Timetable, Bus> {
    
    private final CreateBusFromTimetableItemProcessorHelper createBusItemProcessorHelper;
    private final ScheduleBuilderItemProcessorHelper scheduleBuilderItemProcessorHelper;

    public TimetableToBusItemProcessor(CreateBusFromTimetableItemProcessorHelper createBusItemProcessorHelper, ScheduleBuilderItemProcessorHelper scheduleBuilderItemProcessorHelper) {
        this.createBusItemProcessorHelper = createBusItemProcessorHelper;
        this.scheduleBuilderItemProcessorHelper = scheduleBuilderItemProcessorHelper;
    }

    @Override
    public Bus process(Timetable timetable) throws Exception {
        log.info("Processing timetable [#{} from={}, stop={}, to={}] to Bus", timetable.getBusName(), timetable.getStartBusStopName(), timetable.getActiveStopName(), timetable.getEndBusStopName());

        BusStop busStop = new BusStop();
        log.debug("Setting bus stop properties for stop=[{}] and bus=[{}]", timetable.getActiveStopName(), timetable.getBusName());
        busStop.setBusStopName(timetable.getActiveStopName());
        busStop.setWorkDaySchedule(scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.WEEKDAY_KEY));
        busStop.setSaturdaySchedule(scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SATURDAY_KEY));
        busStop.setSundaySchedule(scheduleBuilderItemProcessorHelper.buildSchedule(timetable, JsoupDocumentToTimetableProcessor.SUNDAY_KEY));

        Bus bus = createBusItemProcessorHelper.createBusFromTimetable(timetable);
        log.debug("Fetching bus route from bus=[#{}]", bus.getBusName());
        BusRoute busRoute = bus.getBusRouteByStartStopName(timetable.getStartBusStopName());
        busRoute.setStartBusStop(timetable.getStartBusStopName());
        busRoute.setEndBusStop(timetable.getEndBusStopName());
        busRoute.addBusStopTimetable(busStop);

        if (bus.hasNoBusRoute(busRoute)) {
            bus.getBusRoutes().add(busRoute);
        }

        log.info("Done processing timetable to bus=[#{}]", bus.getBusName());
        return bus;
    }

}
