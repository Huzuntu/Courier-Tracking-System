package com.courier_service.utils;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

public class GeoUtils 
{
    /**
     * Calculates the distance between two geographical points (latitude and longitude).
     * 
     * @param lat1 Latitude of the first point.
     * @param lng1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lng2 Longitude of the second point.
     * @return Distance in meters between the two points.
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) 
    {
        Coordinate latCoord1 = Coordinate.fromDegrees(lat1);
        Coordinate lngCoord1 = Coordinate.fromDegrees(lng1);
        Point point1 = Point.at(latCoord1, lngCoord1);

        Coordinate latCoord2 = Coordinate.fromDegrees(lat2);
        Coordinate lngCoord2 = Coordinate.fromDegrees(lng2);
        Point point2 = Point.at(latCoord2, lngCoord2);

        return EarthCalc.haversine.distance(point1, point2); // Distance in meters
    }
}
