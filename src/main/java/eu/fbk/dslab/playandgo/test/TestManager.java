package eu.fbk.dslab.playandgo.test;

import java.io.FileWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

@Component
public class TestManager {
	@Value("${startWeek}")
	String startWeek;
	
	@Value("${endWeek}")
	String endWeek;
	
	@Value("${trackDay}")
	String trackDay;
	
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


    @SuppressWarnings("unused")
	public void sendTrack(String mean, String territory, String date, boolean assignSurvey, boolean invitePlayer) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date startDate = sdf.parse(startWeek);
		Date endDate = sdf.parse(endWeek);
		Date trackDate = sdf.parse(trackDay);
		
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

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(trackDate);
		
		List<Long> timestamps = new ArrayList<>();
		for(int j=0; j<6; j++) {
			if(mean.equals("walk")) {
				calendar.add(Calendar.MINUTE, 4);
			} else if(mean.equals("bike")) {
				calendar.add(Calendar.MINUTE, 2);
			} else if(mean.equals("bus")) {
				calendar.add(Calendar.MINUTE, 2);
			}
			timestamps.add(calendar.getTimeInMillis());				
		}

		String track = templateManager.getTrack(mean, tripId, multimodalId, timestamps, territory);

		playAndGoEngine.sendTrack(track);
	}
    	
	public void writeMultimodalTrack(String date) throws Exception {
		String randomId = RandomStringUtils.random(12, true, true);
		String multimodalId = randomId + "_modal";
		String tripId = randomId + "_trip";
		String uuid = RandomStringUtils.random(12, true, true);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
		Date trackDate = sdf.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(trackDate);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 00);
		
		List<Location> locations = new ArrayList<>();
		
		//write bike trip
		createLocations("bike", randomId, calendar, locations);		
		calendar.add(Calendar.MINUTE, 5);
		//write walk trip
		createLocations("walk", randomId, calendar, locations);

		String track = templateManager.getTrackByPolyline(tripId, multimodalId, uuid, locations);
		System.out.println("===== write file =====");
		FileWriter fw = new FileWriter(outputDir + "/" + tripId + ".json", Charset.forName("UTF-8"));
		fw.write(track);
		fw.flush();
		fw.close();
	}
	
	private void createLocations(String mean, String randomId, Calendar calendar, List<Location> locations) throws Exception {
		String tripId = mean + "_" + randomId;
		EncodedPolyline encodedPolyline = null;
		if(mean.equals("walk")) {
			encodedPolyline = new EncodedPolyline(walkPolyline);
		} else if(mean.equals("bike")) {
			encodedPolyline = new EncodedPolyline(bikePolyline);
		}
		List<LatLng> list = encodedPolyline.decodePath();
		
		for(LatLng latLng : list) {
			if(mean.equals("walk")) {
				calendar.add(Calendar.MINUTE, 2);
			} else if(mean.equals("bike")) {
				calendar.add(Calendar.MINUTE, 1);
			}			
			Location location = new Location(latLng.lat, latLng.lng, calendar.getTimeInMillis(), tripId);
			locations.add(location);
		}
	}

}
