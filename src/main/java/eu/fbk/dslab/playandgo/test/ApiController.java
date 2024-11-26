package eu.fbk.dslab.playandgo.test;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
public class ApiController {
	@Autowired
	HereAPITestManager hereTestManager;

	@GetMapping("/api/test/track/send")
	public ResponseEntity<String> sendTrack(
			@Parameter(description = "Starting Date and Time of the track.", example = "2024-11-20T13:00:00")
				@RequestParam String date,
			@Parameter(description = "Latitude and Longitude of the origin point.", example="46.0659,11.1545")
				@RequestParam String origin,
			@Parameter(description = "Latitude and Longitude of the destination point.", example = "46.0342,11.1314")
				@RequestParam String destination,
			@Parameter(description = "Transport Mode. Possible values: walk, bike, car, train, bus or empty(any transport mode).")
				@RequestParam(required = false) String mean,
			@Parameter(description = "Multimodal (multimodal = true) or Polyline of the first section (multimodal=false)")
				@RequestParam(required = false) boolean multimodal
	) throws Exception {
		if(!multimodal) {
			if(StringUtils.isEmpty(mean)) {
				 return new ResponseEntity<>("mean is required", HttpStatus.BAD_REQUEST);
			}
		}
		hereTestManager.sendTrack(mean, date, origin, destination, multimodal);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@GetMapping("/api/test/track/download")
	public ResponseEntity<Resource> downloadTrack(
			@Parameter(description = "Starting Date and Time of the track.", example = "2024-11-20T13:00:00")
				@RequestParam String date,
			@Parameter(description = "Latitude and Longitude of the origin point.", example="46.0659,11.1545")
				@RequestParam String origin,
			@Parameter(description = "Latitude and Longitude of the destination point.", example = "46.0342,11.1314")
				@RequestParam String destination,
			@Parameter(description = "Transport Mode. Possible values: walk, bike, car, train, bus or empty(any transport mode).")
				@RequestParam(required = false) String mean,
			@Parameter(description = "Multimodal (multimodal = true) or Polyline of the first section (multimodal=false)")
				@RequestParam(required = false) boolean multimodal
	) throws Exception {
		if(!multimodal) {
			if(StringUtils.isEmpty(mean)) {
				 return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		String track = hereTestManager.downloadTrack(mean, date, origin, destination, multimodal);
		byte[] content = track.toString().getBytes();
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(content));
		String filename = StringUtils.isEmpty(mean) ?  "track.json" : "track_" + mean + ".json";
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
		return ResponseEntity.ok()
	            .headers(header)
	            .contentLength(content.length)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(resource);		
	}
	
	@GetMapping("/api/test/survey/assign")
	public ResponseEntity<String> assignSurvey(
			@Parameter(description = "Starting Date of the survey.", example = "2024-10-25T01:00:00")
				@RequestParam String startDate,
			@Parameter(description = "End Date of the survey.", example = "2024-10-31T00:00:00")
				@RequestParam String endDate,
			@Parameter(description = "Player Id to assign the survey to.", example = "u_fe939cab-1638-45b3-a604-80a3fb018e54")
				@RequestParam String playerId,
			@Parameter(description = "Campaign Id", example = "TAA.city")
				@RequestParam String campaignId
	) throws Exception {
		hereTestManager.assignSurvey(startDate, endDate, playerId, campaignId);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@GetMapping("/api/test/challenge/invitation")
	public ResponseEntity<String> invitePlayer(
			@Parameter(description = "Player Id to invite.", example = "u_11111")
				@RequestParam String playerToInvite,
			@Parameter(description = "Campaign Id", example = "TAA.city")
				@RequestParam String campaignId
	) throws Exception {
		hereTestManager.invitePlayer(playerToInvite, campaignId);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
}