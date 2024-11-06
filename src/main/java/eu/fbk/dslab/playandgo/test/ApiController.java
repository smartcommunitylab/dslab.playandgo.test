package eu.fbk.dslab.playandgo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	@Autowired
	TestManager testManager;

	@GetMapping("/api/test/track/send")
	public void sendTrack(@RequestParam String mean, 
			@RequestParam String date,
			@RequestParam(required = false) String territory,
			@RequestParam(required = false) boolean assignSurvey,
			@RequestParam(required = false) boolean invitePlayer) throws Exception {
		testManager.sendTrack(mean, territory, date, assignSurvey, invitePlayer);
	}
	
	@GetMapping("/api/test/track/multimodal")
	public void createMultimodalTrip(@RequestParam String date) throws Exception {
		testManager.writeMultimodalTrack(date);
	}
}
