package org.zenbot.szolnok.timetable.batch.step.readurls;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.step.bus.processor.JsoupDocumentService;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableResourceProperties;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

@Slf4j
@Component
@EnableConfigurationProperties(TimetableProperties.class)
public class ReadFileResourcesTasklet implements Tasklet {

    private final StringResourcesInMemoryStorage stringResourcesInMemoryStorage;
    private  final JsoupDocumentService jsoupDocumentService;
    private final TimetableResourceProperties resourceProperties;
    private final TimetableSelectorProperties selectorProperties;

    public ReadFileResourcesTasklet(StringResourcesInMemoryStorage stringResourcesInMemoryStorage, JsoupDocumentService jsoupDocumentService, TimetableProperties properties) {
        this.stringResourcesInMemoryStorage = stringResourcesInMemoryStorage;
        this.jsoupDocumentService = jsoupDocumentService;
        this.resourceProperties = properties.getResource();
        this.selectorProperties = properties.getSelector();
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        log.info("{}",resourceProperties.getSelectedBuses());
        try {
            Document landingPageHtml = jsoupDocumentService.getDocument(resourceProperties.getBaseUrl() + resourceProperties.getSzolnokUrl());
            Elements busLinks = landingPageHtml.select(selectorProperties.getRoutesLinkSelector());
            busLinks.forEach(busLink -> saveBusStopUrlsOfBus(busLink, true));
        } catch (IllegalStateException e) {
            log.error("Could not resolve url=[{}]",resourceProperties.getBaseUrl());
        }

        return RepeatStatus.FINISHED;
    }

    private void saveBusStopUrlsOfBus(Element busLink, boolean contenue) {
        Document busHtmlFile = getBusHtml(resourceProperties.getBaseUrl(), busLink);
        String routeName = busHtmlFile.select(selectorProperties.getRouteNameSelector()).text();
        if (resourceProperties.getSelectedBuses().isEmpty() || resourceProperties.getSelectedBuses().contains(routeName)) {
            log.info("Save urls for bus=[{}]", busHtmlFile.location());
            Elements busStops = busHtmlFile.select(selectorProperties.getStationsSelector());
            saveUrlResource(resourceProperties.getBaseUrl(), busStops);
            if (contenue) {
                Elements otherRoutesLink = busHtmlFile.select(selectorProperties.getOtherRouteSelector());
                otherRoutesLink.forEach(otherRouteLink -> saveBusStopUrlsOfBus(otherRouteLink, false));
            }
        }
    }

    private Document getBusHtml(String baseUrl, Element busLink) {
        String link = busLink.attr(selectorProperties.getHrefSelector());
        return jsoupDocumentService.getDocument(baseUrl + link);
    }

    private void saveUrlResource(String baseUrl, Elements busStops) {
        busStops.forEach(busStop -> {
            String busStopUrl = busStop.attr(selectorProperties.getHrefSelector());
            stringResourcesInMemoryStorage.addUrl(baseUrl + busStopUrl);
        });
    }
}
