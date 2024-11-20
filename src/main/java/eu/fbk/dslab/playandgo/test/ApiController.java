package eu.fbk.dslab.playandgo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;

@RestController
public class ApiController {
	@Autowired
	HereAPITestManager hereTestManager;

	@GetMapping("/api/test/track/send")
	public void sendTrack(
			@RequestParam String mean, 
			@RequestParam String date,
			@RequestParam String origin,
			@RequestParam String destination,
			@RequestParam(required = false) boolean assignSurvey,
			@RequestParam(required = false) boolean invitePlayer,
			@RequestParam(required = false) boolean multimodal) throws Exception {
		hereTestManager.sendTrack(mean, date, origin, destination, assignSurvey, invitePlayer, multimodal);
	}
	
}
