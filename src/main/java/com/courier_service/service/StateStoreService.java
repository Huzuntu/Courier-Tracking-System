package com.courier_service.service;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import com.courier_service.model.courier.AggregatedCourierData;

/**
 * This service is responsible for interacting with Kafka's state store to retrieve aggregated courier data.
 */
@Service
public class StateStoreService 
{
    private KafkaStreams _kafkaStreams;

    /**
     * Constructs a StateStoreService with KafkaStreams instance.
     *
     * @param kafkaStreams    KafkaStreams instance to interact with the state store.
     */
    public StateStoreService(KafkaStreams kafkaStreams)
    {
        this._kafkaStreams = kafkaStreams;
    }

    /**
     * This function retrieves the aggregated data for a specific courier from the state store.
     *
     * @param courierId   The ID of the courier.
     * @return            The aggregated data for the courier.
     */
    public AggregatedCourierData getAggregatedData(Integer courierId) {
        ReadOnlyKeyValueStore<Integer, AggregatedCourierData> store = _kafkaStreams.store(
            StoreQueryParameters.fromNameAndType("courier-data-store",
                                            QueryableStoreTypes.keyValueStore())
        );
        return store.get(courierId);
    }
}
