package com.dnd.mountclim.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class RectanglePoints {

    // nw, ne, sw, se가 들어가 있음.
    private List<LocationPoint> rectanglePoints;
}
