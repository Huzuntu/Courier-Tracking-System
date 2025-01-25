package com.courier_service.repository;

import com.courier_service.model.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  public boolean existsByName(String name);
}
