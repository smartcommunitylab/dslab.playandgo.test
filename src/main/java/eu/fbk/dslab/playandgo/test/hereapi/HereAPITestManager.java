package eu.fbk.dslab.playandgo.test.hereapi;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.fbk.dslab.playandgo.test.PlayAndGoEngine;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class HereAPITestManager {
    @Value("${outputDir}")
    String outputDir;

    @Autowired
    HereAPITemplateManager hereAPITemplateManager;

    @Autowired
    PlayAndGoEngine playAndGoEngine;


    //Polyline: If multimodal is not declared, it is set to false
    public void sendTrack(String mean, String date, String origin, String destination) throws Exception {
        sendTrack(mean, date, origin, destination, false);
    }

    //Multimodal: if mean is not declared, it is set to "" when multimodal is declared
    public void sendTrack(String date, String origin, String destination, boolean multimodal) throws Exception {
        sendTrack("", date, origin, destination, multimodal);
    }

    @SuppressWarnings("unused")
    public void sendTrack(String mean, String date, String origin, String destination, boolean multimodal) throws Exception {

        String uuid = RandomStringUtils.random(12, true, true);

        String track = hereAPITemplateManager.getApiData(mean, date, origin, destination, multimodal);

        if (multimodal) {
            String filePath = outputDir + "/resultDataMultimodal" + mean + ".json";
            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Multimodal Track sent");
        }
        else {
            String filePath = outputDir + "/resultDataPolyline" + mean + ".json";

            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Polyline Track Sent");
        }

        playAndGoEngine.sendTrack(track);
    }

    public void assignSurvey(String startWeek, String endWeek, String playerId, String campaignId) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date startDate = sdf.parse(startWeek);
        Date endDate = sdf.parse(endWeek);

        String assignSurveyJson = hereAPITemplateManager.getAssignSurvey(startDate.getTime(), endDate.getTime());
        playAndGoEngine.assignSurvey(playerId, campaignId, assignSurveyJson);

    }

    public void invitePlayer(String playerToInvite, String campaignId) throws Exception {

        String challengeInviteJson = hereAPITemplateManager.getChallengeInvite(playerToInvite);
        playAndGoEngine.challengeInvite(campaignId, challengeInviteJson);

    }

}

