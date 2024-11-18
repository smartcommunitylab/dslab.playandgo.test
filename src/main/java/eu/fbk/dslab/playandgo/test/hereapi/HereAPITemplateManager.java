package eu.fbk.dslab.playandgo.test.hereapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponse;
import eu.fbk.dslab.playandgo.test.hereapi.polyline.PolylineEncoderDecoder;
import eu.fbk.dslab.playandgo.test.hereapi.service.HereAPIService;

@Component
public class HereAPITemplateManager {

    @Value("${originTrento:}")
    private String originTrento;

    @Value("${destinationTrento:}")
    private String destinationTrento;

    @Value("${departureTime:}")
    private String departureTime;


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
    public List<String> getOriginAndDestination(String territory) throws IllegalArgumentException {

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



    /**
     * Retrieves the route data from the Here API and generates a string for sending data to the server.
     *
     * @param mean the mean of transport for which to get the route data
     * @param date the date for which to get the route data
     * @param tripId the id of the trip
     * @param multimodalId the id of the multimodal trip
     * @param territory the territory for which to get the route data
     * @param polyline whether to use polyline
     * @return a string to send to the server
     * @throws JsonProcessingException if there is a problem with the JSON processing
     * @throws ParseException if there is a problem with the parsing of the date
     */
    public String getApiData(String mean, String date, String tripId, String multimodalId, String territory, boolean polyline) throws JsonProcessingException, ParseException {

    	String origin = getOriginAndDestination(territory).get(0);
    	String destination = getOriginAndDestination(territory).get(1);

        HereAPIResponse hereAPIResponse = hereAPIService.fetchRouteData(mean, date, origin, destination);

        Map<String, Object> variables = new HashMap<>();
        variables.put("trip", tripId);
        variables.put("multimodal", multimodalId);
        variables.put("city", territory);
        variables.put("mean", mean);
        variables.put("uuid", UUID.randomUUID().toString());

        String mode = hereAPIService.getMode(mean);

        if (!polyline) {

            if (mode.equals("bus")) {
                List<HereAPIResponse.TimePoint> allTimePoints = new ArrayList<>();
                for (HereAPIResponse.Route route : hereAPIResponse.getRoutes()) {
                    for (HereAPIResponse.Section section : route.getSections()) {
                        if (section.getTransport().getMode().equals(mode)) {
                            HereAPIResponse.TimePoint departure = section.getDeparture();
                            allTimePoints.add(departure);
                        }
                        if (section.getIntermediateStops() != null) {
                            for(HereAPIResponse.IntermediateStop stop : section.getIntermediateStops()) {
                                allTimePoints.add(stop.getDeparture());
                            }
                            HereAPIResponse.TimePoint lastPoint = section.getArrival();
                            allTimePoints.add(lastPoint);
                        }

                    }
                }

                variables.put("points", allTimePoints);

            }
            else if (mode.equals("pedestrian") || mode.equals("bicycle")) {
                List<HereAPIResponse.TimePoint> allTimePoints = new ArrayList<>();
                for (HereAPIResponse.Route route : hereAPIResponse.getRoutes()) {
                    for (HereAPIResponse.Section section : route.getSections()){
                        if (section.getTransport().getMode().equals(mode)) {
                            HereAPIResponse.TimePoint departure = section.getDeparture();
                            HereAPIResponse.TimePoint arrival = section.getArrival();
                            allTimePoints.add(departure);
                            allTimePoints.add(arrival);
                        }
                    }
                }

                variables.put("points", allTimePoints);

            }

            if(StringUtils.isNotEmpty(territory)) {
                return getContent("hereapi/send-track-" + mean + "-" + territory + ".txt", variables);
            }
            else {
                return getContent("hereapi/send-track-" + mean + ".txt", variables);
            }


        }
        else {
            String departure = "";
            String arrival = "";
            String encodedPolyline = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            for (HereAPIResponse.Route route : hereAPIResponse.getRoutes()) {
                for (HereAPIResponse.Section section : route.getSections()) {
                    if (section.getTransport().getMode().equals(mode)) {
                        departure = section.getDeparture().getTime();
                        arrival = section.getArrival().getTime();
                        encodedPolyline = section.getPolyline();
                    }
                }
            }

            variables.put("polyline", encodedPolyline);

            List<HereAPIResponse.TimePoint> points = getPolylineLocations(encodedPolyline);

            variables.put("points", points);


            Date startDate = sdf.parse(departure);
            Date endDate = sdf.parse(arrival);

            long travelTimeMillis = endDate.getTime() - startDate.getTime();
            long segmentTime = travelTimeMillis / ((long) points.size() - 1);
            int segmentTimeInt = (int) (segmentTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for(HereAPIResponse.TimePoint point : points) {
            	point.setTime(String.valueOf(cal.getTime().getTime()));
            	 cal.add(Calendar.MILLISECOND, segmentTimeInt);
            }

            return getContent("hereapi/send-track-polyline.txt", variables);
        }

    }



/**
 * Decodes an encoded polyline string into a list of Location objects.
 *
 * @param encodedPolyline the URL-safe encoded polyline string representing a series of geographical coordinates
 * @return a list of Location objects, each containing latitude and longitude coordinates derived from the decoded polyline
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


}
