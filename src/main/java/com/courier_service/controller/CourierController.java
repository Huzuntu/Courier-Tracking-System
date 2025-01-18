package com.courier_service.controller;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courier_service.model.courier.AggregatedCourierData;
import com.courier_service.model.courier.CourierLocation;
import com.courier_service.service.StateStoreService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/couriers")
public class CourierController 
{
    private KafkaTemplate<Integer, CourierLocation> _kafkaTemplate;
    private final StateStoreService _stateStoreService;

    /**
     * Constructs a CourierController with dependencies injected.
     *
     * @param kafkaTemplate     KafkaTemplate for sending courier locations.
     * @param stateStoreService Service to retrieve aggregated courier data.
     */
    public CourierController(KafkaTemplate<Integer, CourierLocation> kafkaTemplate, StateStoreService stateStoreService)
    {
        this._kafkaTemplate = kafkaTemplate;
        this._stateStoreService = stateStoreService;
    }

    /**
     * Endpoint to log a courier's location by sending it to Kafka.
     *
     * @param location The CourierLocation to be logged.
     * @return A success message.
     */
    @PostMapping("/log")
    public String logLocation(@RequestBody CourierLocation location) 
    {
        location.setTimestamp(LocalDateTime.now());
        _kafkaTemplate.send("input", location.getCourierId(), location);
        return "Location logged successfully";
    }

    /**
     * Endpoint to retrieve the total distance traveled by a courier.
     *
     * @param courierId The ID of the courier.
     * @return A message with the total distance traveled by the courier.
     */
    @GetMapping("/distance-travelled/{id}")
    public String getTotalTravelDistance(@PathVariable Integer courierId) 
    {
        AggregatedCourierData aggregatedCourierData = _stateStoreService.getAggregatedData(courierId);

        if(aggregatedCourierData == null)
        {
            return String.format("Courier with ID: %d, is not found in the state store", courierId);
        }

        double totalDistance = aggregatedCourierData.getTotalDistance();

        return String.format("Courier %d has traveled a total distance of %.2f meters.", courierId, totalDistance);
    }
    
}
