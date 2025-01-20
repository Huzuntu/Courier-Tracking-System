package com.courier_service.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.courier_service.model.store.Store;

@Repository
public class StoreRepository
{
    private final List<Store> storeList = new ArrayList<>();

    /**
     * Checks if a store with the given name already exists in the list.
     *
     * @param name The name of the store.
     * @return true if the store exists, false otherwise.
     */
    public boolean existsByName(String name) {
        return storeList.stream().anyMatch(store -> store.getName().equals(name));
    }

    /**
     * Adds a store to the in-memory store list.
     *
     * @param store The store to add.
     */
    public void add(Store store) {
        storeList.add(store);
    }

    /**
     * Retrieves all stores in the list.
     *
     * @return a list of all stores.
     */
    public List<Store> findAll() {
        return new ArrayList<>(storeList);
    }
}
