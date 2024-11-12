package eu.fbk.dslab.playandgo.test.hereapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Component
public class HereAPITemplateManager {

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

    public String getApiData(String mean, String tripId, String multimodalId, String territory) throws JsonProcessingException {

        String dataJson = hereAPIService.fetchRouteData_AsJson();
        HereAPIResponse hereAPIResponse = objectMapper.readValue(dataJson, HereAPIResponse.class);

        Map<String, Object> variables = new HashMap<>();
        variables.put("trip", tripId);
        variables.put("multimodal", multimodalId);
        variables.put("city", territory);

        variables.put("sections", hereAPIResponse.getRoutes().get(0).getSections());


        if(StringUtils.isNotEmpty(territory)) {
            return getContent("send-track-" + mean + "-" + territory + ".txt", variables);
        }
        else {
            return getContent("send-track-" + mean + ".txt", variables);
        }

    }



}
