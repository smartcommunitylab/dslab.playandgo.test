package eu.fbk.dslab.playandgo.test;

import org.junit.jupiter.api.Test;

import com.google.maps.model.EncodedPolyline;

public class TestPolyline {
	
	@Test
	public void decodePolyline() {
		EncodedPolyline encodedPolyline = new EncodedPolyline("_p~iF~ps|U_ulLnnqC_mqNvxq`@");
		encodedPolyline.decodePath().forEach(point -> {
			System.out.println(String.format("[%s,%s]", point.lat, point.lng));
		});
	}
	
}
