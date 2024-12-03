package eu.fbk.dslab.playandgo.test.hereapi.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HereAPIResponse {
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
        private List<IntermediateStop> intermediateStops;
        private Agency agency;
        private List<Attribution> attributions;
     }

    @Data
    @NoArgsConstructor
    public static class Attribution {
        private String id;
        private String href;
        private String text;
        private String type;
    }

    @Data
    @NoArgsConstructor
    public static class TimePoint {
        private String time;
        private Place place;
        private String tripId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Place {
        private String name;
        private String type;
        private String description;
        private Location location;
        private Location originalLocation;
        private String wheelchairAccessible;
        private String id;
        private String code;
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
        private String name;
        private String category;
        private String color;
        private String textColor;
        private String headsign;
        private String shortName;
        private String longName;
        private String wheelchairAccessible;        
    }

    @Data
    @NoArgsConstructor
    public static class IntermediateStop {
        private String duration;
        private TimePoint departure;
    }

    @Data
    @NoArgsConstructor
    public static class Agency {
        private String id;
        private String name;
        private String website;
    }
}
