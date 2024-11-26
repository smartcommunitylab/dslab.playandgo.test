package eu.fbk.dslab.playandgo.test;

import eu.fbk.dslab.playandgo.test.hereapi.HereAPITestManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private HereAPITestManager hereAPITestManager;

	@Value("${departureTime}")
	String departureTime;

	String origin = "46.0659,11.1545";
	String destination = "46.0342,11.1314";
	@Test
	public void sendPolyline() throws Exception {
		hereAPITestManager.sendTrack("bus", departureTime, origin, destination);
	}
	@Test
	public void sendMultimodal() throws Exception {
		hereAPITestManager.sendTrack("car",departureTime, origin, destination, true);

	}
    
}
