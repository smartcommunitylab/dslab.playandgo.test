package eu.fbk.dslab.playandgo.test.hereapi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HereAPIResponseWalkBike {
    private List<Route> routes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Route {
        private String id;
        private List<Section> sections;
    }

    @Data
    @NoArgsConstructor
    public static class Section {
        private String id;
        private String type;
        private TimePoint departure;
        private TimePoint arrival;
        private String polyline;
        private Transport transport;
    }

    @Data
    @NoArgsConstructor
    public static class TimePoint {
        private String time;
        private Place place;
    }

    @Data
    @NoArgsConstructor
    public static class Place {
        private String type;
        private Location location;
        private Location originalLocation;
    }

    @Data
    @NoArgsConstructor
    public static class Location {
        private double lat;
        private double lng;
    }

    @Data
    @NoArgsConstructor
    public static class Transport {
        private String mode;
    }

}