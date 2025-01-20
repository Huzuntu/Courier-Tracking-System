package com.courier_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import com.courier_service.config.serdes.CourierLocationSerde;

import lombok.Data;

@Data
@EnableKafka
@EnableKafkaStreams
@Configuration
public class KafkaStreamsConfig 
{
    private String inputTopic = "input";
    private String outputTopic = "output";
	
	/**
     * Configures Kafka Streams with the necessary properties to process data.
     *
     * @return KafkaStreamsConfiguration for Kafka Streams application.
     */
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kStreamsConfig() 
	{
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "courier-location-stream");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CourierLocationSerde.class.getName());

		return new KafkaStreamsConfiguration(props);
    }

}

