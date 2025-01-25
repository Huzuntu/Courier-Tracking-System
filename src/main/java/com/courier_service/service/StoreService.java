package com.courier_service.service;

import com.courier_service.model.store.Store;
import com.courier_service.repository.StoreRepository;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
  private final StoreRepository storeRepository;

  public StoreService(StoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  @Cacheable("stores")
  public List<Store> fetchStoresFromCache() {
    return storeRepository.findAll();
  }

  @CacheEvict(value = "stores", allEntries = true)
  public void clearCache() {
    System.out.println("Cache cleared successfully.");
  }

  @Scheduled(fixedRate = 60 * 60 * 1000) // Refresh every hour
  public void refreshCachePeriodically() {
    clearCache();
    fetchStoresFromCache();
    System.out.println("Cache refreshed successfully at: " + System.currentTimeMillis());
  }
}
