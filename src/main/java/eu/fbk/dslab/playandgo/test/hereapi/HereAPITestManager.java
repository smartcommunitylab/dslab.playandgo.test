package eu.fbk.dslab.playandgo.test.hereapi;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.fbk.dslab.playandgo.test.PlayAndGoEngine;
import eu.fbk.dslab.playandgo.test.TemplateManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class HereAPITestManager {
    @Value("${startWeek}")
    String startWeek;

    @Value("${endWeek}")
    String endWeek;

    @Value("${playerToInvite}")
    String playerToInvite;

    @Value("${playerId}")
    String playerId;

    @Value("${campaignId}")
    String campaignId;

    @Value("${outputDir}")
    String outputDir;

    @Autowired
    TemplateManager templateManager;

    @Autowired
    PlayAndGoEngine playAndGoEngine;

    @Autowired
    HereAPITemplateManager hereAPITemplateManager;



    public void sendTrack(String mean, String date, String origin, String destination, boolean assignSurvey, boolean invitePlayer) throws Exception {
        sendTrack(mean, date, origin, destination, assignSurvey, invitePlayer, false);
    }

    @SuppressWarnings("unused")
    public void sendTrack(String mean, String date, String origin, String destination, boolean assignSurvey, boolean invitePlayer, boolean multimodal) throws Exception {

        String uuid = RandomStringUtils.random(12, true, true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date startDate = sdf.parse(startWeek);
        Date endDate = sdf.parse(endWeek);

        if(assignSurvey) {
            String assignSurveyJson = templateManager.getAssignSurvey(startDate.getTime(), endDate.getTime());
            playAndGoEngine.assignSurvey(playerId, campaignId, assignSurveyJson);
            Thread.sleep(1000);
        }

        if(invitePlayer) {
            String challengeInviteJson = templateManager.getChallengeInvite(playerToInvite);
            playAndGoEngine.challengeInvite(campaignId, challengeInviteJson);
            Thread.sleep(1000);
        }

        String track = hereAPITemplateManager.getApiData(mean, date, origin, destination, multimodal);

        if (multimodal) {
            String filePath = outputDir + "/resultDataMultimodal" + mean + ".json";

            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Multimodal Track sent");
        }
        else {
            String filePath = outputDir + "/resultDataPolyline2" + mean + ".json";

            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Polyline Track Sent");
        }

        playAndGoEngine.sendTrack(track);
    }



}

