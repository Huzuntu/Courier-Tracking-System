package com.courier_service.config.serdes;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.courier_service.model.courier.AggregatedCourierData;

public class AggregatedCourierDataSerde extends Serdes.WrapperSerde<AggregatedCourierData>
{
    public AggregatedCourierDataSerde() 
    {
        super(new JsonSerializer<>(), new JsonDeserializer<>(AggregatedCourierData.class));
    }
}
