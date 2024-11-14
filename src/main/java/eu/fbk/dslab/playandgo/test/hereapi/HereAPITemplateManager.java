package eu.fbk.dslab.playandgo.test.hereapi;

import java.util.*;

import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponseBus;
import eu.fbk.dslab.playandgo.test.hereapi.service.HereAPIService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HereAPITemplateManager {

    @Value("${originTrento:}")
    private String originTrento;

    @Value("${destinationTrento:}")
    private String destinationTrento;


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HereAPIService hereAPIService;

    private final ObjectMapper objectMapper;

    public HereAPITemplateManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getContent(String template, Map<String, Object> variables) {
        final Context ctx = new Context();
        ctx.setVariables(variables);
        return templateEngine.process(template, ctx);
    }

    public List<String> getOriginDestination(String territory) throws IllegalArgumentException {

        List<String> points = new ArrayList<>();
        String origin = "";
        String destination = "";

        if(StringUtils.isNotEmpty(territory)) {
            if (territory.equals("trento")) {
                origin = originTrento;
                destination = destinationTrento;
            }
            else {
                throw new IllegalArgumentException("territory not supported");
            }
        }
        else {
            throw new IllegalArgumentException("territory cannot be null");
        }

        points.add(origin);
        points.add(destination);

        return points;
    }


    public String getApiData(String mean, String date, String tripId, String multimodalId, String territory) throws JsonProcessingException {

    	String origin = getOriginDestination(territory).get(0);
    	String destination = getOriginDestination(territory).get(1);

        HereAPIResponseBus hereAPIResponseBus = new HereAPIResponseBus();
/*
        HereAPIResponseWalkBike hereAPIResponseWalkBike = new HereAPIResponseWalkBike();
*/

        if (mean.equals("bus")) {
            hereAPIResponseBus = hereAPIService.fetchRouteDataBus(mean, date, origin, destination);

        }
        /*else if (mean.equals("walk") || mean.equals("bike")) {
            hereAPIResponseWalkBike = hereAPIService.fetchRouteDataWalkBike(mean, date, origin, destination);
        }*/

        Map<String, Object> variables = new HashMap<>();
        variables.put("trip", tripId);
        variables.put("multimodal", multimodalId);
        variables.put("city", territory);
        variables.put("mean", mean);
        variables.put("uuid", UUID.randomUUID().toString());

        String mode = hereAPIService.getMode(mean);

        if (mode.equals("bus")) {
            List<HereAPIResponseBus.TimePoint> allTimePoints = new ArrayList<>();
            for (HereAPIResponseBus.Route route : hereAPIResponseBus.getRoutes()) {
                for (HereAPIResponseBus.Section section : route.getSections()) {
                    if (section.getTransport().getMode().equals(mode)) {
                        HereAPIResponseBus.TimePoint departure = section.getDeparture();
                        allTimePoints.add(departure);
                    }
                    if (section.getIntermediateStops() != null) {
                        for(HereAPIResponseBus.IntermediateStop stop : section.getIntermediateStops()) {
                            allTimePoints.add(stop.getDeparture());
                        }
                        HereAPIResponseBus.TimePoint lastPoint = section.getArrival();
                        allTimePoints.add(lastPoint);
                    }

                }
            }

            variables.put("points", allTimePoints);

        }
        /*else if (mode.equals("pedestrian") || mode.equals("bicycle")) {
            List<HereAPIResponseWalkBike.TimePoint> allTimePoints = new ArrayList<>();
            for (HereAPIResponseWalkBike.Route route : hereAPIResponseWalkBike.getRoutes()) {
                for (HereAPIResponseWalkBike.Section section : route.getSections()) {
                    HereAPIResponseWalkBike.TimePoint departure = section.getDeparture();
                    allTimePoints.add(departure);
                }
            }

            variables.put("points", allTimePoints);

        }*/


        if(StringUtils.isNotEmpty(territory)) {
            return getContent("send-track-" + mean + "-" + territory + ".txt", variables);
        }
        else {
            return getContent("send-track-" + mean + ".txt", variables);
        }

    }

}
