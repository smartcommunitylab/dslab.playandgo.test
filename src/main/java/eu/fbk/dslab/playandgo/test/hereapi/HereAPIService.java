package eu.fbk.dslab.playandgo.test.hereapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    public String fetchRouteData_AsString() {
        String url = hereApiUrl + "&apiKey=" + hereApiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return responseEntity.getBody();
    }

    public String fetchRouteData_AsJson() throws JsonProcessingException {
        String url = hereApiUrl + "&apiKey=" + hereApiKey;

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        HereAPIResponse hereAPIResponse = objectMapper.readValue(responseEntity.getBody(), HereAPIResponse.class);

        return objectMapper.writeValueAsString(hereAPIResponse);
    }

    public void saveJsonToFile() throws IOException {
        String jsonString = fetchRouteData_AsJson();

        String filePath = outputDir + "/routeData.json";

        Files.write(Paths.get(filePath), jsonString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }



}
