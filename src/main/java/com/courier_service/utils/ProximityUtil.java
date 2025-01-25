package com.courier_service.utils;

import com.courier_service.model.courier.CourierLocation;
import com.courier_service.model.store.Store;
import com.courier_service.model.store.StoreEntry;
import java.time.Duration;
import java.util.List;

public class ProximityUtil {
  public static boolean hasRecentlyEntered(
      List<StoreEntry> storeEntries, Store store, CourierLocation newLocation) {
    return storeEntries.stream()
        .anyMatch(
            entry ->
                entry.getStoreName().equals(store.getName())
                    && Duration.between(entry.getEntryTime(), newLocation.getTimestamp())
                            .toMinutes()
                        < 1);
  }
}
