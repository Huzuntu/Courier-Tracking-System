package com.courier_service.service;

import org.springframework.stereotype.Service;

import com.courier_service.model.courier.AggregatedCourierData;
import com.courier_service.model.courier.CourierLocation;
import com.courier_service.utils.GeoUtils;

@Service
public class DistanceService 
{
    /**
     * Updates the total distance traveled by a courier.
     *
     * @param lastLocation         Previous location of the courier.
     * @param newLocation          Current location of the courier.
     * @return Updated total distance traveled.
     */
    public void updateTotalDistance(AggregatedCourierData aggregatedCourierData, CourierLocation newLocation) 
    {
        CourierLocation lastLocation = aggregatedCourierData.getLastLocation();
        if (lastLocation != null) {
            double distance = calculateDistance(lastLocation, newLocation);
            aggregatedCourierData.setTotalDistance(aggregatedCourierData.getTotalDistance() + distance);
        }
    }
    
    /**
     * Calculates the distance between two locations.
     *
     * @param lastLocation  Previous location of the courier.
     * @param newLocation   Current location of the courier.
     * @return Distance in meters between the two locations.
     */
    public double calculateDistance(CourierLocation lastLocation, CourierLocation newLocation) 
    {
        if (lastLocation == null || newLocation == null) {
            return 0.0;
        }
        return GeoUtils.calculateDistance(
                lastLocation.getLat(), lastLocation.getLng(),
                newLocation.getLat(), newLocation.getLng()
        );
    }


}
