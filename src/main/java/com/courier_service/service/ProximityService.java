package com.courier_service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.courier_service.model.courier.CourierLocation;
import com.courier_service.model.store.Store;
import com.courier_service.model.store.StoreEntry;
import com.courier_service.repository.StoreRepository;
import com.courier_service.utils.GeoUtils;

@Service
public class ProximityService 
{
    private final StoreRepository _storeRepository;

    public ProximityService(StoreRepository storeRepository)
    {
        this._storeRepository = storeRepository;
    }

    /**
     * Checks if a courier is within proximity to any store and records the entry.
     *
     * @param courierId      ID of the courier.
     * @param newLocation    Current location of the courier.
     * @param storeEntries   List of recent store entries by the courier.
     * @return List of updated store entries.
     */
    public List<StoreEntry> checkProximityToStores(Integer courierId, CourierLocation newLocation, List<StoreEntry> storeEntries) 
    {
        List<Store> stores = _storeRepository.findAll();

        for (Store store : stores) 
        {
            double distanceToStore = GeoUtils.calculateDistance(newLocation.getLat(), newLocation.getLng(), store.getLat(), store.getLng());

            if (distanceToStore <= 100) // 100-meter radius
            { 
                boolean recentlyEntered = storeEntries.stream()
                    .anyMatch(entry -> entry.getStoreName().equals(store.getName()) &&
                            Duration.between(entry.getEntryTime(), newLocation.getTimestamp()).toMinutes() < 1);

                if (!recentlyEntered) 
                {
                    storeEntries.add(new StoreEntry(store.getName(), newLocation.getTimestamp()));
                }
            }
        }

        // Cleanup old entries beyond a certain timeframe, e.g., 10 minutes
        storeEntries = storeEntries.stream()
                .filter(entry -> Duration.between(entry.getEntryTime(), LocalDateTime.now()).toMinutes() <= 10)
                .collect(Collectors.toList());

        return storeEntries;
    }
}