package com.courier_service.service;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.courier_service.model.courier.AggregatedCourierData;
import com.courier_service.model.courier.CourierLocation;
import com.courier_service.model.store.StoreEntry;

import lombok.extern.slf4j.Slf4j;


/**
 * This service is responsible for processing courier data, finding the total distance
 * traveled by couriers from first entry and 
 * determining if logged courier's have entered a store's proximity.
 */
@Configuration
@Slf4j
public class CourierService
{
    private final ProximityService _proximityService;
    private final DistanceService _distanceService;

    /**
     * Constructs a CourierService with dependencies injected.
     *
     * @param proximityService    Service responsible for store proximity calculations.
     * @param distanceService     Service responsible for distance calculations.
     */
    public CourierService(ProximityService proximityService, DistanceService distanceService)
    {
        this._proximityService = proximityService;
        this._distanceService = distanceService;
    }



    /**
     * This function processes the courier's new location and updates the total distance traveled.
     * It also checks if the courier is near any stores and adds an store entry if necessary.
     *
     * @param courierId          The ID of the courier.
     * @param newLocation        The new location of the courier.
     * @param aggregatedData     The aggregated data for the courier.
     * @return Updated aggregated data.
     */
    public AggregatedCourierData processCourierData(Integer courierId, CourierLocation newLocation, AggregatedCourierData aggregatedData) 
    {
        // Calculate the total distance traveled by the courier
        _distanceService.updateTotalDistance(aggregatedData, newLocation);

        // Check proximity to stores and update store entries
        List<StoreEntry> updatedStoreEntries = _proximityService.checkProximityToStores(courierId, newLocation, aggregatedData.getRecentStoreEntries());
        aggregatedData.setRecentStoreEntries(updatedStoreEntries);

        // Update the last known location
        aggregatedData.setLastLocation(newLocation);

        return aggregatedData;
    }
}
