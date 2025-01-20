package com.courier_service.model.courier;

import java.util.ArrayList;
import java.util.List;

import com.courier_service.model.store.StoreEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedCourierData 
{
    private CourierLocation lastLocation;
    private double totalDistance;
    private List<StoreEntry> recentStoreEntries = new ArrayList<>();
}
