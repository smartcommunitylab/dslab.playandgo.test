package eu.fbk.dslab.playandgo.test.hereapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HereAPIConfiguration {
    
    /**
     * A Spring RestTemplate for making HTTP requests to the HERE API.
     *
     * @return a new RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * An instance of the Jackson ObjectMapper for JSON (de)serialization.
     *
     * @return a new ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
