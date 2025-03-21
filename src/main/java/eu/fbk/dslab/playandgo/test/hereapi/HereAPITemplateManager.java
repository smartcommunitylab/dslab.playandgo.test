package eu.fbk.dslab.playandgo.test.hereapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponse;
import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponse.TimePoint;
import eu.fbk.dslab.playandgo.test.hereapi.polyline.PolylineEncoderDecoder;
import eu.fbk.dslab.playandgo.test.hereapi.service.HereAPIService;

@Component
public class HereAPITemplateManager {

    /*@Value("${originTrento:}")
    private String originTrento;

    @Value("${destinationTrento:}")
    private String destinationTrento;*/


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HereAPIService hereAPIService;

    /**
     * Get the content of the template with the given name by processing it with
     * the given variables.
     *
     * @param template the name of the template
     * @param variables the variables to pass to the template
     * @return the content of the template
     */
    private String getContent(String template, Map<String, Object> variables) {
        final Context ctx = new Context();
        ctx.setVariables(variables);
        return templateEngine.process(template, ctx);
    }

    /**
     * Retrieves the origin and destination points for the specified territory.
     *
     * @param territory the name of the territory for which to get the origin and destination
     * @return a list containing the origin and destination points
     * @throws IllegalArgumentException if the territory is null or not supported
     */
    /*public List<String> getOriginAndDestination(String territory) throws IllegalArgumentException {

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
    }*/


    /**
     * Given a mean of transportation, a date, an origin, a destination, a multimodal flag and an invalid track flag,
     * this method fetches the route data from the HERE API and processes it to return a JSON object containing the
     * coordinates of the track points.
     *
     * @param mean the mean of transportation (e.g. walk, bike, car, train, bus)
     * @param date the departure date for the route
     * @param origin the starting point of the route
     * @param destination the ending point of the route
     * @param multimodal if true, all the transport modes are included in the route
     * @param invalidTrack if true, the travel time is divided by 20
     * @return a JSON object containing the coordinates of the track points
     * @throws JsonProcessingException if there is an error processing the JSON response
     * @throws ParseException if there is an error parsing the date
     */
    public String getApiData(String mean, String date, String origin, String destination, boolean multimodal, boolean invalidTrack) throws JsonProcessingException, ParseException {

        String multimodalId = RandomStringUtils.random(12, true, true) + "_modal";

        HereAPIResponse hereAPIResponse = hereAPIService.fetchRouteData(mean, date, origin, destination);

        Map<String, Object> variables = new HashMap<>();
        variables.put("uuid", UUID.randomUUID().toString());

        String mode = hereAPIService.getMode(mean);
        List<HereAPIResponse.TimePoint> points = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        if (!hereAPIResponse.getRoutes().isEmpty()) {
            for (HereAPIResponse.Section section : hereAPIResponse.getRoutes().get(0).getSections()) {
                if (!multimodal && !mode.contains(section.getTransport().getMode()))
                	continue;
            	
            	String tripId = convertToMean(section.getTransport().getMode()) + "_" + RandomStringUtils.random(12, true, true);
            	
            	List<TimePoint> sectionPoints = getPolylineLocations(section.getPolyline());
            	
                Date startDate = sdf.parse(section.getDeparture().getTime());
                Date endDate = sdf.parse(section.getArrival().getTime());
                long travelTimeMillis = endDate.getTime() - startDate.getTime();
                if (invalidTrack) {
                    travelTimeMillis = travelTimeMillis / 20;
                }
                long segmentTime = travelTimeMillis / ((long) sectionPoints.size() - 1);
                int segmentTimeInt = (int) (segmentTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                for(HereAPIResponse.TimePoint point : sectionPoints) {
                    point.setTime(String.valueOf(cal.getTime().getTime()));
                    point.setTripId(tripId);
                    cal.add(Calendar.MILLISECOND, segmentTimeInt);
                }
                points.addAll(sectionPoints);
                
                if (!multimodal && mode.contains(section.getTransport().getMode()))
                	break;
            }
        }

        variables.put("multimodal", multimodalId);
        variables.put("points", points);
        return getContent("hereapi/send-track-template.txt", variables);


    }
    
    public HereAPIResponse getApiRowData(String mean, String date, String origin, String destination) throws JsonProcessingException {
    	return hereAPIService.fetchRouteData(mean, date, origin, destination);
    }
    
    public String createJson(HereAPIResponse hereAPIResponse, String mean, String date, boolean multimodal) throws ParseException {
    	String multimodalId = RandomStringUtils.random(16, true, true) + "_modal";
        Map<String, Object> variables = new HashMap<>();
        variables.put("uuid", UUID.randomUUID().toString());

        String mode = hereAPIService.getMode(mean);
        List<HereAPIResponse.TimePoint> points = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        if (!hereAPIResponse.getRoutes().isEmpty()) {
            for (HereAPIResponse.Section section : hereAPIResponse.getRoutes().get(0).getSections()) {
                if (!multimodal && !mode.contains(section.getTransport().getMode()))
                	continue;
            	
            	String tripId = convertToMean(section.getTransport().getMode()) + "_" + RandomStringUtils.random(16, true, true);
            	
            	List<TimePoint> sectionPoints = getPolylineLocations(section.getPolyline());
            	
                Date startDate = sdf.parse(section.getDeparture().getTime());
                Date endDate = sdf.parse(section.getArrival().getTime());
                long travelTimeMillis = endDate.getTime() - startDate.getTime();
                long segmentTime = travelTimeMillis / ((long) sectionPoints.size() - 1);
                int segmentTimeInt = (int) (segmentTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                for(HereAPIResponse.TimePoint point : sectionPoints) {
                    point.setTime(String.valueOf(cal.getTime().getTime()));
                    point.setTripId(tripId);
                    cal.add(Calendar.MILLISECOND, segmentTimeInt);
                }
                points.addAll(sectionPoints);
                
                if (!multimodal && mode.contains(section.getTransport().getMode()))
                	break;
            }
        }        
        variables.put("multimodal", multimodalId);
        variables.put("points", points);
        return getContent("hereapi/send-track-template.txt", variables);    	
    }

    /**
     * Decodes an encoded polyline string and converts it into a list of TimePoint objects.
     * Each TimePoint object contains a Place, which holds the latitude and longitude
     * extracted from the decoded polyline coordinates.
     *
     * @param encodedPolyline the encoded polyline string representing a sequence of geographical points
     * @return a list of TimePoint objects with their associated Place and Location data
     */
    public List<HereAPIResponse.TimePoint> getPolylineLocations(String encodedPolyline) {

        List<PolylineEncoderDecoder.LatLngZ> coordinates = PolylineEncoderDecoder.decode(encodedPolyline);

        List<HereAPIResponse.TimePoint> points = new ArrayList<>();

        for (PolylineEncoderDecoder.LatLngZ latLngZ : coordinates) {
        	HereAPIResponse.TimePoint point = new HereAPIResponse.TimePoint();
        	HereAPIResponse.Place place = new HereAPIResponse.Place();
            HereAPIResponse.Location loc = new HereAPIResponse.Location();
            loc.setLat(latLngZ.lat);
            loc.setLng(latLngZ.lng);
            place.setLocation(loc);
            point.setPlace(place);
            points.add(point);
        }

        return points;
    }

    public String convertToMean(String mode) {
        String mean;
        if(StringUtils.isNotEmpty(mode)) {
            Set<String> trainModes = Set.of(
                    "highSpeedTrain",
                    "intercityTrain",
                    "interRegionalTrain",
                    "regionalTrain",
                    "cityTrain"
            );

            switch (mode) {
                case "pedestrian":
                    mean = "walk";
                    break;
                case "bicycle":
                    mean = "bike";
                    break;
                case "bus":
                    mean = "bus";
                    break;
                case "car":
                    mean = "car";
                    break;
                default:
                    if (trainModes.contains(mode)) {
                        mean = "train";
                    } else {
                        throw new IllegalArgumentException("mode not supported");
                    }
            }
        }
        else {
            throw new IllegalArgumentException("mode cannot be null");
        }

        return mean;
    }

    public String getAssignSurvey(Long startWeek, Long endWeek) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fromDate", startWeek);
        variables.put("toDate", endWeek);
        return getContent("assign-survey.txt", variables);
    }

    public String getChallengeInvite(String playerId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("playerId", playerId);
        return getContent("challenge-invite.txt", variables);
    }


}
