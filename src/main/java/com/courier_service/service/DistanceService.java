package com.courier_service.service;

import com.courier_service.model.courier.CourierLocation;
import com.courier_service.utils.GeoUtils;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {
  /**
   * Updates the total distance traveled by a courier.
   *
   * @param lastLocation Previous location of the courier.
   * @param newLocation Current location of the courier.
   * @return Updated total distance traveled.
   */
  public double getTotalDistanceForCourier(
      CourierLocation lastLocation, CourierLocation newLocation, double totalDistance) {
    // CourierLocation lastLocation = aggregatedCourierData.getLastLocation();
    if (lastLocation != null) {
      double distance = calculateDistance(lastLocation, newLocation);
      return totalDistance + distance;
      // aggregatedCourierData.setTotalDistance(aggregatedCourierData.getTotalDistance() +
      // distance);
    }
    return totalDistance;
  }

  /**
   * Calculates the distance between two locations.
   *
   * @param lastLocation Previous location of the courier.
   * @param newLocation Current location of the courier.
   * @return Distance in meters between the two locations.
   */
  public double calculateDistance(CourierLocation lastLocation, CourierLocation newLocation) {
    if (lastLocation == null || newLocation == null) {
      return 0.0;
    }
    return GeoUtils.calculateDistance(
        lastLocation.getLat(), lastLocation.getLng(),
        newLocation.getLat(), newLocation.getLng());
  }
}
