package com.dnd.secondgo.domain.dto;

import lombok.Getter;

@Getter
public class LocationPoint {

    private Double latitude;
    private Double longitude;

    public LocationPoint(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
