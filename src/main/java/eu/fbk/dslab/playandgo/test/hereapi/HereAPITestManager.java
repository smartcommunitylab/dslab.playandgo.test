package eu.fbk.dslab.playandgo.test.hereapi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.fbk.dslab.playandgo.test.PlayAndGoEngine;
import eu.fbk.dslab.playandgo.test.hereapi.domain.HereAPIResponse;


@Component
public class HereAPITestManager {
    @Autowired
    HereAPITemplateManager hereAPITemplateManager;

    @Autowired
    PlayAndGoEngine playAndGoEngine;


    //Polyline: If multimodal is not declared, it is set to false
    public void sendTrack(String mean, String date, String origin, String destination) throws Exception {
        sendTrack(mean, date, origin, destination, false, false);
    }

    //Multimodal: if mean is not declared, it is set to "" when multimodal is declared
    public void sendTrack(String date, String origin, String destination, boolean multimodal) throws Exception {
        sendTrack("", date, origin, destination, multimodal, false);
    }

    //If invalidTrack is not declared, it is set to false
    public void sendTrack(String mean, String date, String origin, String destination, boolean multimodal) throws Exception {
        sendTrack(mean, date, origin, destination, multimodal, false);
    }

    //Polyline: If multimodal is not declared, it is set to false
    public String downloadTrack(String mean, String date, String origin, String destination) throws Exception {
        return downloadTrack(mean, date, origin, destination, false, false);
    }
    
    //Multimodal: if mean is not declared, it is set to "" when multimodal is declared
    public String downloadTrack(String date, String origin, String destination, boolean multimodal) throws Exception {
        return downloadTrack("", date, origin, destination, multimodal, false);
    }
    
    //If invalidTrack is not declared, it is set to false
    public String downloadTrack(String mean, String date, String origin, String destination, boolean multimodal) throws Exception {
        return downloadTrack(mean, date, origin, destination, multimodal, false);
    }

    //Main Send and Download Track
    //Main Function Send Track
    public void sendTrack(String mean, String date, String origin, String destination, boolean multimodal, boolean invalidTrack) throws Exception {
        String track = hereAPITemplateManager.getApiData(mean, date, origin, destination, multimodal, invalidTrack);
        playAndGoEngine.sendTrack(track);
    }

    //Main Function Download Track
    public String downloadTrack(String mean, String date, String origin, String destination, boolean multimodal, boolean invalidTrack) throws Exception {
        return hereAPITemplateManager.getApiData(mean, date, origin, destination, multimodal, invalidTrack);
    }
    
    public void sendBulk(String mean, String date, String origin, String destination, boolean multimodal, int iterations) throws Exception {
    	HereAPIResponse apiRowData = hereAPITemplateManager.getApiRowData(mean, date, origin, destination);
    	for (int i = 0; i < iterations; i++) {
    		String track = hereAPITemplateManager.createJson(apiRowData, mean, date, multimodal);
    		playAndGoEngine.sendTrack(track);
    		Thread.sleep(1000);
		}
    }

    //Assign Survey
    public void assignSurvey(String startWeek, String endWeek, String playerId, String campaignId) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date startDate = sdf.parse(startWeek);
        Date endDate = sdf.parse(endWeek);

        String assignSurveyJson = hereAPITemplateManager.getAssignSurvey(startDate.getTime(), endDate.getTime());
        playAndGoEngine.assignSurvey(playerId, campaignId, assignSurveyJson);

    }


    //Invite Player
    public void invitePlayer(String playerToInvite, String campaignId) throws Exception {

        String challengeInviteJson = hereAPITemplateManager.getChallengeInvite(playerToInvite);
        playAndGoEngine.challengeInvite(campaignId, challengeInviteJson);

    }

}

