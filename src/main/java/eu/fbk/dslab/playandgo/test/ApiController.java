package eu.fbk.dslab.playandgo.test;

import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;

@RestController
public class ApiController {
	@Autowired
	HereAPITestManager hereTestManager;

	@GetMapping("/api/test/track/send")
	public ResponseEntity<String> sendTrack(
			@Parameter(description = "Starting Date and Time of the track.", example = "2024-11-20T13:00:00")
				@RequestParam(value = "date") String date,
			@Parameter(description = "Latitude and Longitude of the origin point.", example="46.0659,11.1545")
				@RequestParam(value = "origin") String origin,
			@Parameter(description = "Latitude and Longitude of the destination point.", example = "46.0342,11.1314")
				@RequestParam(value = "destination") String destination,
			@Parameter(description = "Transport Mode. Possible values: walk, bike, car, train, bus or empty(any transport mode).")
				@RequestParam(value = "mean", required = false) String mean,
			@Parameter(description = "Assign user to Survey. Use false.")
				@RequestParam(value = "assignSurvey", required = false) boolean assignSurvey,
			@Parameter(description = "Invite player to challenge. Use false.")
				@RequestParam(value = "invitePlayer", required = false) boolean invitePlayer,
			@Parameter(description = "Multimodal (multimodal = true) or Polyline of the first section (multimodal=false)")
				@RequestParam(value = "multimodal", required = false) boolean multimodal
	) throws Exception {
		if(!multimodal) {
			if(StringUtils.isEmpty(mean)) {
				 return new ResponseEntity<>("mean is required", HttpStatus.BAD_REQUEST);
			}
		}
		hereTestManager.sendTrack(mean, date, origin, destination, assignSurvey, invitePlayer, multimodal);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
}
