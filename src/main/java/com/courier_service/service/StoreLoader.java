package com.courier_service.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.courier_service.model.store.Store;
import com.courier_service.repository.StoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreLoader
{
    private final StoreRepository storeRepository;

    /**
     * Loads store data from a JSON file and adds it to an in-memory store list.
     * If stores are already present, it does not add duplicates.
     *
     * @throws IOException If there's an error reading the JSON file.
     */
    @PostConstruct
    public void loadStoreData() throws IOException 
    {
        // Load JSON file from the resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stores.json");
        if (inputStream == null) 
        {
            throw new FileNotFoundException("stores.json file not found in resources");
        }

        // Convert JSON to List of Store objects using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        List<Store> stores = objectMapper.readValue(inputStream, new TypeReference<List<Store>>() {});

        // Adds stores to an in-memory store list, avoiding duplicates
        for (Store store : stores) 
        {
            if (!storeRepository.existsByName(store.getName())) 
            {
                storeRepository.add(store);
            }
        }
    }
}