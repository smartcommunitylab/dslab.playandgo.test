package eu.fbk.dslab.playandgo.test;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITemplateManager;
import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	TestManager testManager;
    @Autowired
    private HereAPITestManager hereAPITestManager;



	@Value("${departureTime}")
	String departureTime;

	@Test
    public void sendTracks() throws Exception {

		hereAPITestManager.sendTrack("bus", "trento", departureTime, false , false);

	}

	@Test
	public void sendPolyline() throws Exception {

		hereAPITestManager.sendTrack("bus", "trento", departureTime, true);

	}
    
}
