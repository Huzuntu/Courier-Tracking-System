package com.courier_service.service;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import com.courier_service.model.courier.AggregatedCourierData;

/**
 * This service is responsible for interacting with Kafka's state store to retrieve aggregated courier data.
 */
@Service
public class StateStoreService 
{
    private final StreamsBuilderFactoryBean factoryBean;

    /**
     * Constructs a StateStoreService with KafkaStreams instance.
     *
     * @param factoryBean    Factory bean instance to interact with the state store.
     */
    public StateStoreService(StreamsBuilderFactoryBean factoryBean) 
    {
        this.factoryBean = factoryBean;
    }

    /**
     * This function retrieves the aggregated data for a specific courier from the state store.
     *
     * @param courierId   The ID of the courier.
     * @return            The aggregated data for the courier.
     */
    public AggregatedCourierData getAggregatedData(Integer courierId) 
    {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        if (kafkaStreams == null) {
            throw new IllegalStateException("KafkaStreams instance is not yet initialized");
        }

        ReadOnlyKeyValueStore<Integer, AggregatedCourierData> store = kafkaStreams.store(
            StoreQueryParameters.fromNameAndType("courier-data-store", QueryableStoreTypes.keyValueStore())
        );
        return store.get(courierId);
    }
}
