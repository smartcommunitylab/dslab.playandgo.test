package eu.fbk.dslab.playandgo.test.hereapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class HereAPIConfiguration {

    /**
     * Configures and returns an OpenAPI bean that provides metadata
     * for the Play&Go Test Project API documentation.
     *
     * @return an OpenAPI object containing API information and external documentation.
     */
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Play&Go Test Project")
                        .version("2.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("SmartCommunityLab")
                        .url("https://www.smartcommunitylab.it/"));
    }
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
