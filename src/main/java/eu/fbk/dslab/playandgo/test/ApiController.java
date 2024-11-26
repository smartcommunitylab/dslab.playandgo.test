package eu.fbk.dslab.playandgo.test;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;

import javax.validation.Valid;

@RestController
public class ApiController {
	@Autowired
	HereAPITestManager hereTestManager;

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", content = @Content) })
	@GetMapping("/api/test/track/send")
	public ResponseEntity<String> sendTrack(
			@NotNull @Parameter(description = "Starting Date and Time of the track. Format: \"yyyy-MM-dd'T'HH:mm:ss\"")
				@Valid @RequestParam(value = "date") String date,
			@NotNull @Parameter(description = "Latitude and Longitude of the origin point. Format example: \"01.0000,01.0000\"")
				@Valid @RequestParam(value = "origin") String origin,
			@NotNull @Parameter(description = "Latitude and Longitude of the destination point. Format example: \"01.0000,01.0000\"")
				@Valid @RequestParam(value = "destination") String destination,
			@NotNull @Parameter(description = "Transport Mode. Possible values: walk, bike, car, train, bus.")
				@Valid() @RequestParam(value = "mean", required = false, defaultValue = "") String mean,
			@Parameter(description = "Assign user to Survey. Use false.")
				@RequestParam(value = "assignSurvey", required = false, defaultValue = "false") boolean assignSurvey,
			@Parameter(description = "Invite player to challenge. Use false.")
				@RequestParam(value = "invitePlayer", required = false, defaultValue = "false") boolean invitePlayer,
			@Parameter(description = "Choose if you want the result to be Multimodal (multimodal = true) or just one Polyline of the first section (multimodal=false)")
				@RequestParam(value = "multimodal", required = false, defaultValue = "true") boolean multimodal
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
