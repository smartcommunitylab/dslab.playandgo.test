package eu.fbk.dslab.playandgo.test;

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
			@RequestParam String date,
			@RequestParam String origin,
			@RequestParam String destination,
			@RequestParam(required = false) String mean, 
			@RequestParam(required = false) boolean assignSurvey,
			@RequestParam(required = false) boolean invitePlayer,
			@RequestParam(required = false) boolean multimodal) throws Exception {
		if(!multimodal) {
			if(StringUtils.isEmpty(mean)) {
				 return new ResponseEntity<>("mean is required", HttpStatus.BAD_REQUEST);
			}
		}
		hereTestManager.sendTrack(mean, date, origin, destination, assignSurvey, invitePlayer, multimodal);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
}
