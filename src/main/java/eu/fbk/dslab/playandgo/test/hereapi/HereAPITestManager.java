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


    /**
     * Sends a track to the PlayAndGo engine with the given mean of transportation, territory, start date of the week,
     * and flags to assign a survey or invite a player to the campaign.
     *
     * @param mean The transportation mean (e.g., "walk", "bike").
     * @param territory The territory or city for the track data.
     * @param date The start date of the week in the format "YYYY-MM-DD".
     * @param assignSurvey Whether to assign a survey to the player.
     * @param invitePlayer Whether to invite the player to the campaign.
     */
    public void sendTrack(String mean, String territory, String date, boolean assignSurvey, boolean invitePlayer) throws Exception {
        sendTrack(mean, territory, date, assignSurvey, invitePlayer, true);
    }



    /**
     * Sends a track to the PlayAndGo engine.
     *
     * @param mean The transportation mean (e.g., "walk", "bike").
     * @param territory The territory or city for the track data.
     * @param date The date of the track in "yyyy-MM-dd'T'HH:mm" format.
     * @param polyline If true, processes the track as a polyline.
     * @throws Exception If there is an error in track processing.
     */
    public void sendTrack(String mean, String territory,  String date, boolean polyline) throws Exception {
        sendTrack(mean, territory,  date, false, false, polyline);
    }




    /**
     * Sends a track to the PlayAndGo engine.
     *
     * @param mean The transportation mean (e.g., "walk", "bike").
     * @param territory The territory or city for the track data.
     * @param date The date of the track in "yyyy-MM-dd'T'HH:mm" format.
     * @param assignSurvey If true, assigns a survey to the player.
     * @param invitePlayer If true, invites the player to a challenge.
     * @param polyline If true, processes the track as a polyline.
     * @throws Exception If there is an error in track processing or file writing.
     */
    @SuppressWarnings("unused")
    public void sendTrack(String mean, String territory, String date, boolean assignSurvey, boolean invitePlayer, boolean polyline) throws Exception {

        String tripId = mean + "_" + RandomStringUtils.random(12, true, true);
        String multimodalId = tripId + "_modal";
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

        String track = hereAPITemplateManager.getApiData(mean, date, tripId, multimodalId, territory, polyline);

        if (!polyline) {
            String filePath = outputDir + "/resultDataTemplate" + mean + ".json";

            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Track sent");
        }
        else {
            String filePath = outputDir + "/resultDataPolyline" + mean + ".json";

            Files.write(Paths.get(filePath), track.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Polyline Track Sent");
        }

        playAndGoEngine.sendTrack(track);
    }



}

