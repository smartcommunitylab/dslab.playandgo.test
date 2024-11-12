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

    @Value("${walk_polyline}")
    String walkPolyline;

    @Value("${bike_polyline}")
    String bikePolyline;

    @Value("${outputDir}")
    String outputDir;

    @Autowired
    TemplateManager templateManager;

    @Autowired
    PlayAndGoEngine playAndGoEngine;

    @Autowired
    HereAPITemplateManager hereAPITemplateManager;


    @SuppressWarnings("unused")
    public void sendTrack(String mean, String territory, String date, boolean assignSurvey, boolean invitePlayer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date startDate = sdf.parse(startWeek);
        Date endDate = sdf.parse(endWeek);
        Date trackDate = sdf.parse(date);

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

        String tripId = mean + "_" + RandomStringUtils.random(12, true, true);
        String multimodalId = tripId + "_modal";
        String uuid = RandomStringUtils.random(12, true, true);


        String track = hereAPITemplateManager.getApiData(mean, tripId, multimodalId, territory);

        String filePath = outputDir + "/routeDataTemplate.json";

        Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Done");
        /*	playAndGoEngine.sendTrack(track);*/
    }


}

