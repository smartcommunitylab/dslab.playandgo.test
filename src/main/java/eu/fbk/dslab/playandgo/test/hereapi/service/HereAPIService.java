package eu.fbk.dslab.playandgo.test.hereapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class HereAPIService {

    @Value("${hereTransitApiUrl}")
    String hereTransitApiUrl;

    @Value("${hereRouteApiUrl}")
    String hereRouteApiUrl;

    @Value("${hereApiKey}")
    String hereApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HereAPIService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }



    /**
     * Translates a mean of transportation (e.g. walk, bike, car, train, bus) into a mode that can be used in the HERE API.
     * @param mean the mean of transportation
     * @return the equivalent mode for HERE API
     */
    public String getMode(String mean) {

        String mode = "";
        if(StringUtils.isNotEmpty(mean)) {
            switch (mean) {
                case "walk":
                    mode = "pedestrian";
                    break;
                case "bike":
                    mode = "bicycle";
                    break;
                case "bus":
                    mode = "bus";
                    break;
                case "car":
                    mode = "car";
                    break;
                case "train":
                    mode = "highSpeedTrain,intercityTrain,interRegionalTrain,regionalTrain,cityTrain";
                    break;
                default:
                    break;
            }
        }
        return mode;
    }

    /**
     * Given a mode of transportation, origin, destination, departure time, and mean, build the corresponding HERE API URL
     * @param mode the mode of transportation
     * @param origin the origin of the route
     * @param destination the destination of the route
     * @param departureTime the departure time of the route
     * @param mean the mean of transportation
     * @return the URL of the HERE API request
     */
    public String getUrl(String mode, String origin, String destination, String departureTime, String mean) {
        String url;
        if (mode.equals("pedestrian") || mode.equals("bicycle") || mode.equals("car")) {
            url = hereRouteApiUrl + "?apiKey=" + hereApiKey
                    + "&origin=" + origin + "&destination=" + destination
                    + "&departureTime=" + departureTime
                    + "&transportMode=" + mode
                    + "&return=polyline";
        }
        else if (mode.isEmpty()) {
            url = hereTransitApiUrl + "?apiKey=" + hereApiKey
                    + "&origin=" + origin + "&destination=" + destination
                    + "&departureTime=" + departureTime
                    + "&return=polyline,intermediate";
        }
        else  {
            url = hereTransitApiUrl + "?apiKey=" + hereApiKey
                    + "&origin=" + origin + "&destination=" + destination
                    + "&departureTime=" + departureTime
                    + "&modes=" + getMode(mean)
                    + "&return=polyline,intermediate";
        }

        return url;
    }



    /**
     * Fetches route data from the Here API based on the provided transportation mean, departure time,
     * origin, and destination. This method constructs the appropriate API URL, sends a GET request,
     * and processes the response to return a HereAPIResponse object.
     *
     * @param mean the mean of transportation (either "walk", "bike", or "bus")
     * @param departureTime the departure time for the route
     * @param origin the starting point of the route
     * @param destination the ending point of the route
     * @return a HereAPIResponse object containing the route data
     * @throws JsonProcessingException if there is an error processing the JSON response
     */
    public HereAPIResponse fetchRouteData(String mean, String departureTime, String origin, String destination) throws JsonProcessingException {

        String mode = getMode(mean);

        String url = getUrl(mode, origin, destination, departureTime, mean);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return objectMapper.readValue(responseEntity.getBody(), HereAPIResponse.class);
    }

}
