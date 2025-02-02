package com.courier_service.consumer;

import com.courier_service.config.KafkaConfig.KafkaStreamsConfig;
import com.courier_service.config.serdes.AggregatedCourierDataSerde;
import com.courier_service.config.serdes.CourierLocationSerde;
import com.courier_service.model.courier.AggregatedCourierData;
import com.courier_service.model.courier.CourierLocation;
import com.courier_service.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Slf4j
public class CourierConsumer {

  private final KafkaStreamsConfig kafkaStreamsConfig;
  private final CourierService courierService;

  /**
   * Constructs a CourierConsumer with dependencies injected.
   *
   * @param kafkaStreamsConfig Configuration for Kafka Streams.
   * @param courierService Service to process courier data.
   */
  public CourierConsumer(KafkaStreamsConfig kafkaStreamsConfig, CourierService courierService) {
    this.kafkaStreamsConfig = kafkaStreamsConfig;
    this.courierService = courierService;
  }

  /**
   * Configures a Kafka Streams processor that listens to the input topic, processes courier data,
   * and outputs the updated aggregated data to the output topic.
   *
   * @param kStreamsBuilder StreamsBuilder for configuring the stream processing.
   * @return KStream that processes the courier data.
   */
  @Bean
  public KStream<Integer, CourierLocation> kStream(StreamsBuilder kStreamsBuilder) {
    KStream<Integer, CourierLocation> stream =
        kStreamsBuilder.stream(
            kafkaStreamsConfig.getInputTopic(),
            Consumed.with(Serdes.Integer(), new CourierLocationSerde()));

    stream
        .groupByKey(Grouped.with(Serdes.Integer(), new CourierLocationSerde()))
        .aggregate(
            () -> new AggregatedCourierData(),
            (courierId, newLocation, aggregatedData) -> {
              return courierService.processCourierLocation(courierId, newLocation, aggregatedData);
            },
            Materialized.<Integer, AggregatedCourierData, KeyValueStore<Bytes, byte[]>>as(
                    "courier-data-stores")
                .withKeySerde(Serdes.Integer())
                .withValueSerde(new AggregatedCourierDataSerde()))
        .toStream()
        .peek((key, value) -> log.info("Courier Data Updated: {}", value))
        .to(
            kafkaStreamsConfig.getOutputTopic(),
            Produced.with(Serdes.Integer(), new AggregatedCourierDataSerde()));

    return stream;
  }
}
