package eu.fbk.dslab.playandgo.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
	@Value("${trackDay}")
	String trackDay;

	@Autowired
	TestManager testManager;
	
	@Test
    public void sendTracks() throws Exception {
		testManager.sendTracks("bike", "");
	}
    
    @Test
	public void sendTrackByPolyline() throws Exception {
    	testManager.sendTrackByPolyline("bike", trackDay);
	}
    
}
