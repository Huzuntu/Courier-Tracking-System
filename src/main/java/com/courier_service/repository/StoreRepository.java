package com.courier_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courier_service.model.store.Store;

public interface StoreRepository extends JpaRepository<Store, Long>
{
    /**
     * Checks if a store with the given name already exists in the database.
     *
     * @param name The name of the store.
     * @return true if the store exists, false otherwise.
     */
    boolean existsByName(String name);
}
