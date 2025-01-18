package com.courier_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.courier_service.model.courier.CourierLocation;

@EnableKafka
@Configuration
public class KafkaProducerConfig 
{
    /**
     * Configures and provides a Kafka producer factory for sending courier location data.
     *
     * @return KafkaProducerFactory for CourierLocation with Integer as the key and CourierLocation as the value.
     */
    @Bean
    public ProducerFactory<Integer, CourierLocation> producerFactory() 
    {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Configures and provides a KafkaTemplate for sending CourierLocation messages to Kafka.
     *
     * @return KafkaTemplate for sending CourierLocation messages.
     */
    @Bean
    public KafkaTemplate<Integer, CourierLocation> kafkaTemplate() 
    {
        return new KafkaTemplate<>(producerFactory());
    }    
}