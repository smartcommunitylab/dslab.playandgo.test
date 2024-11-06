package eu.fbk.dslab.playandgo.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
	@Autowired
	TestManager testManager;
	
	@Test
    public void sendTracks() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		testManager.sendTrack("bike", null, sdf.format(new Date()), false , false);
	}
    
}
