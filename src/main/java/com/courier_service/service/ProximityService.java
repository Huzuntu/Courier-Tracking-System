package com.courier_service.service;

import com.courier_service.model.courier.CourierLocation;
import com.courier_service.model.store.Store;
import com.courier_service.model.store.StoreEntry;
import com.courier_service.repository.StoreRepository;
import com.courier_service.utils.GeoUtils;
import com.courier_service.utils.ProximityUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProximityService {
  public static final int PROXIMITY_RADIUS_METERS = 100;
  private final StoreRepository storeRepository;

  public ProximityService(StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  /**
   * Checks if a courier is within proximity to any store and records the entry.
   *
   * @param courierId ID of the courier.
   * @param newLocation Current location of the courier.
   * @param storeEntries List of recent store entries by the courier.
   * @return List of updated store entries.
   */
  public List<StoreEntry> checkProximityToStores(
      Integer courierId, CourierLocation newLocation, List<StoreEntry> storeEntries) {
    List<Store> stores = storeRepository.findAll();

    for (Store store : stores) {
      double distanceToStore =
          GeoUtils.calculateDistance(
              newLocation.getLat(), newLocation.getLng(), store.getLat(), store.getLng());

      if (distanceToStore <= PROXIMITY_RADIUS_METERS) // 100-meter radius
      {
        boolean recentlyEntered =
            ProximityUtil.hasRecentlyEntered(storeEntries, store, newLocation);

        if (!recentlyEntered) {
          storeEntries.add(new StoreEntry(store.getName(), newLocation.getTimestamp()));
        }
      }
    }

    return storeEntries;
  }
}
