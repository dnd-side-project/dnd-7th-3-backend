package com.dnd.secondgo.domain.service;

import com.dnd.secondgo.domain.dto.LocationPoint;
import com.dnd.secondgo.domain.dto.RectanglePoints;
import com.dnd.secondgo.domain.util.GeometryUtil;
import com.dnd.secondgo.domain.vo.Direction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {
    public RectanglePoints getRectanglePoints(double lat, double lng, double radius){
        RectanglePoints points = new RectanglePoints();

        radius = radius / 2;

        LocationPoint northWest = GeometryUtil
                .calculate(lat, lng, radius, Direction.NORTHWEST.getBearing());
        LocationPoint northEast = GeometryUtil
                .calculate(lat, lng, radius, Direction.NORTHEAST.getBearing());
        LocationPoint southWest = GeometryUtil
                .calculate(lat, lng, radius, Direction.SOUTHWEST.getBearing());
        LocationPoint southEast = GeometryUtil
                .calculate(lat, lng, radius, Direction.SOUTHEAST.getBearing());

        List<LocationPoint> rp = new ArrayList<>();
        rp.add(northWest);
        rp.add(northEast);
        rp.add(southWest);
        rp.add(southEast);

        points.setRectanglePoints(rp);

        return points;
    }

    public List<RectanglePoints> getRectanglePoints(RectanglePoints points, double radius){

        List<RectanglePoints> rp = new ArrayList<>();

        for(LocationPoint lp : points.getRectanglePoints()){
            rp.add(getRectanglePoints(lp.getLatitude(), lp.getLongitude(), radius));
        }

        return rp;
    }
}
