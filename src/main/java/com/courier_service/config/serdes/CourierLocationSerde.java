package com.courier_service.config.serdes;

import com.courier_service.model.courier.CourierLocation;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CourierLocationSerde extends Serdes.WrapperSerde<CourierLocation> {
  public CourierLocationSerde() {
    super(new JsonSerializer<>(), new JsonDeserializer<>(CourierLocation.class));
  }
}
