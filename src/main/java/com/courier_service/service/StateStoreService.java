package com.courier_service.service;

import com.courier_service.model.courier.AggregatedCourierData;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.SmartLifecycle;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for interacting with Kafka's state store to retrieve aggregated
 * courier data.
 */
@Service
public class StateStoreService implements SmartLifecycle {
  private final StreamsBuilderFactoryBean factoryBean;
  private ReadOnlyKeyValueStore<Integer, AggregatedCourierData> stateStore;
  private boolean running = false;

  public StateStoreService(StreamsBuilderFactoryBean factoryBean) {
    this.factoryBean = factoryBean;
  }

  @Override
  public void start() {
    KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
    if (kafkaStreams == null) {
      throw new IllegalStateException("KafkaStreams instance is not yet initialized");
    }
    this.stateStore =
        kafkaStreams.store(
            StoreQueryParameters.fromNameAndType(
                "courier-data-stores", QueryableStoreTypes.keyValueStore()));
    this.running = true;
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public int getPhase() {
    return Integer.MAX_VALUE;
  }

  /**
   * This function retrieves the total distance travelled for a specific courier from the state
   * store.
   *
   * @param courierId The ID of the courier.
   * @return The total distance travelled by the courier.
   */
  public double calculateDistanceForCourier(Integer courierId) {
    if (!isRunning()) {
      throw new IllegalStateException("StateStoreService is not running");
    }
    AggregatedCourierData data = stateStore.get(courierId);
    if (data == null) {
      throw new IllegalArgumentException("Courier not found: " + courierId);
    }
    return data.getTotalDistance();
  }
}
