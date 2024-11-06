package eu.fbk.dslab.playandgo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TemplateManager {
	
    @Autowired 
    private TemplateEngine templateEngine;

    private String getContent(String template, Map<String, Object> variables) {
    	final Context ctx = new Context();
    	ctx.setVariables(variables);
    	return templateEngine.process(template, ctx); 
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

    public String getTrack(String mean, String tripId, String multimodalId, List<Long> timestamps, String territory) {
    	Map<String, Object> variables = new HashMap<>();
    	variables.put("trip", tripId);
    	variables.put("multimodal", multimodalId);
    	variables.put("tracks", timestamps);
		variables.put("city", territory);

		if(StringUtils.isNotEmpty(territory)) {
			return getContent("send-track-" + mean + "-" + territory + ".txt", variables);
		}
		else {
			return getContent("send-track-" + mean + ".txt", variables);
		}

    }
    
    public String getTrackByPolyline(String tripId, String multimodalId, String uuid, List<Location> locations) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("locations", locations);
    	variables.put("tripId", tripId);
    	variables.put("multimodalId", multimodalId);
    	variables.put("uuid", uuid);
		return getContent("send-track-templ.txt", variables);
    }
    
}
