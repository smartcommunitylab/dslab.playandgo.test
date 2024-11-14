package eu.fbk.dslab.playandgo.test.hereapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponseBus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class HereAPIService {

    @Value("${outputDir}")
    String outputDir;

    @Value("${hereApiUrl}")
    String hereApiUrl;

    @Value("${hereApiKey}")
    String hereApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HereAPIService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getMode(String mean) {

        String mode = "";
        if(StringUtils.isNotEmpty(mean)) {
            if(mean.equals("walk")) {
                mode = "pedestrian";
            }
            else if (mean.equals("bike")) {
                mode = "bicycle";
            }
            else if(mean.equals("bus")) {
                mode = "bus";
            }
            else {
                throw new IllegalArgumentException("mean not supported");
            }
        }
        else {
            throw new IllegalArgumentException("mean cannot be null");
        }

        return mode;
    }

    public String getUrl(String mode, String origin, String destination, String departureTime, String mean) {
        String url = "";

        if (mode.equals("bus") ){
            url = hereApiUrl + "?apiKey=" + hereApiKey
                    + "&origin=" + origin + "&destination=" + destination
                    + "&departureTime=" + departureTime
                    + "&modes=" + getMode(mean)
                    + "&return=polyline,intermediate";
        }
        /*else if (mode.equals("pedestrian") || mode.equals("bicycle")) {
            url = "https://router.hereapi.com/v8/routes" + "?apiKey=" + hereApiKey
                    + "&origin=" + origin + "&destination=" + destination
                    + "&departureTime=" + departureTime
                    + "&transportMode=" + mode
                    + "&return=polyline";
        }*/

        return url;
    }



    public HereAPIResponseBus fetchRouteDataBus(String mean, String departureTime, String origin, String destination) throws JsonProcessingException {

        String mode = getMode(mean);

        String url = getUrl(mode, origin, destination, departureTime, mean);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return objectMapper.readValue(responseEntity.getBody(), HereAPIResponseBus.class);
    }

   /* public HereAPIResponseWalkBike fetchRouteDataWalkBike(String mean, String departureTime, String origin, String destination) throws JsonProcessingException {

        String mode = getMode(mean);

        String url = getUrl(mode, origin, destination, departureTime, mean);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return objectMapper.readValue(responseEntity.getBody(), HereAPIResponseWalkBike.class);
    }*/

}
