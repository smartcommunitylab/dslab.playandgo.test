package eu.fbk.dslab.playandgo.test.hereapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;
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


    public String getApiData(String mean, String date, String origin, String destination, boolean multimodal) throws JsonProcessingException, ParseException {

        String tripId = "";
        String multimodalId = "";

        HereAPIResponse hereAPIResponse = hereAPIService.fetchRouteData(mean, date, origin, destination);

        Map<String, Object> variables = new HashMap<>();
        variables.put("uuid", UUID.randomUUID().toString());

        String mode = hereAPIService.getMode(mean);
        List<HereAPIResponse.TimePoint> points = new ArrayList<>();
        List<List<HereAPIResponse.TimePoint>> allPoints = new ArrayList<>();
        List<HereAPIResponse.Section> modeSections = new ArrayList<>();
        List<String> tripIds = new ArrayList<>();
        List<String> means = new ArrayList<>();

        String departurePolyline = "";
        String arrivalPolyline = "";
        String encodedPolyline = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();

        /*if (!multimodal) {
            for (HereAPIResponse.Route route : hereAPIResponse.getRoutes()) {
                for (HereAPIResponse.Section section : route.getSections()) {
                    if (section.getTransport().getMode().equals(mode)) {
                        modeSections.add(section);
                        departurePolyline = modeSections.get(0).getDeparture().getTime();
                        arrivalPolyline = modeSections.get(0).getArrival().getTime();
                        encodedPolyline = modeSections.get(0).getPolyline();
                        tripId = convertToMean(modeSections.get(0).getTransport().getMode()) + "_" + RandomStringUtils.random(12, true, true);
                        tripIds.add(tripId);
                    }

                }
            }

            points = getPolylineLocations(encodedPolyline);

            Date startDate = sdf.parse(departurePolyline);
            Date endDate = sdf.parse(arrivalPolyline);

            long travelTimeMillis = endDate.getTime() - startDate.getTime();
            long segmentTime = travelTimeMillis / ((long) points.size() - 1);
            int segmentTimeInt = (int) (segmentTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for(HereAPIResponse.TimePoint point : points) {
                point.setTime(String.valueOf(cal.getTime().getTime()));
                cal.add(Calendar.MILLISECOND, segmentTimeInt);
            }
            means.add(mean);
        }*/

        for (HereAPIResponse.Route route : hereAPIResponse.getRoutes()) {
            for (HereAPIResponse.Section section : route.getSections()) {
                if (!multimodal && section.getTransport().getMode().equals(mode)) {
                    if (modeSections.isEmpty()) {
                        modeSections.add(section);
                        departurePolyline = modeSections.get(0).getDeparture().getTime();
                        arrivalPolyline = modeSections.get(0).getArrival().getTime();
                        encodedPolyline = modeSections.get(0).getPolyline();
                        tripId = convertToMean(modeSections.get(0).getTransport().getMode()) + "_" + RandomStringUtils.random(12, true, true);
                        tripIds.add(tripId);
                        startDate = sdf.parse(departurePolyline);
                        endDate = sdf.parse(arrivalPolyline);
                        points = getPolylineLocations(encodedPolyline);
                        means.add(mean);
                        multimodalId = tripId + "_modal";

                        long travelTimeMillis = endDate.getTime() - startDate.getTime();
                        long segmentTime = travelTimeMillis / ((long) points.size() - 1);
                        int segmentTimeInt = (int) (segmentTime);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDate);
                        for(HereAPIResponse.TimePoint point : points) {
                            point.setTime(String.valueOf(cal.getTime().getTime()));
                            cal.add(Calendar.MILLISECOND, segmentTimeInt);
                        }
                        allPoints.add(points);
                    }
                }
                else if (multimodal) {
                    tripId = convertToMean(section.getTransport().getMode()) + "_" + RandomStringUtils.random(12, true, true);
                    tripIds.add(tripId);
                    points = getPolylineLocations(section.getPolyline());
                    startDate = sdf.parse(section.getDeparture().getTime());
                    endDate = sdf.parse(section.getArrival().getTime());
                    modeSections.add(section);
                    means.add(convertToMean(section.getTransport().getMode()));

                    long travelTimeMillis = endDate.getTime() - startDate.getTime();
                    long segmentTime = travelTimeMillis / ((long) points.size() - 1);
                    int segmentTimeInt = (int) (segmentTime);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    for(HereAPIResponse.TimePoint point : points) {
                        point.setTime(String.valueOf(cal.getTime().getTime()));
                        cal.add(Calendar.MILLISECOND, segmentTimeInt);
                    }
                    allPoints.add(points);
                    multimodalId = tripIds.get(0) + "_modal";
                }
            }
        }

        variables.put("multimodal", multimodalId);
        variables.put("sections", modeSections);
        variables.put("tripIds", tripIds);
        variables.put("points", allPoints);
        variables.put("means", means);
        return getContent("hereapi/send-track-template.txt", variables);


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


}
