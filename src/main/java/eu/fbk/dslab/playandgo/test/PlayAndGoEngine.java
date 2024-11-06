package eu.fbk.dslab.playandgo.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlayAndGoEngine {
	@Value("${token}")
	String token;
	
	@Value("${apiEndopint}")
	String apiEndopint;
	
	public void assignSurvey(String playerId, String campaignId, String content) throws Exception {
		String url = apiEndopint + "/survey/assign?campaignId=" + campaignId + "&playerIds=" + playerId;
		HTTPConnector.doTokenAuthenticationPost(url, content, token);
	}
	
	public void challengeInvite(String campaignId, String content) throws Exception {
		String url = apiEndopint + "/challenge/invitation?campaignId=" + campaignId;
		HTTPConnector.doTokenAuthenticationPost(url, content, token);		
	}
	
	public void sendTrack(String content) throws Exception {
		String url = apiEndopint + "/track/player/geolocations";
		HTTPConnector.doTokenAuthenticationPost(url, content, token);		
	}

}
