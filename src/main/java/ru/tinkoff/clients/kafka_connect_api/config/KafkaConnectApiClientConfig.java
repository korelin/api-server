package ru.tinkoff.clients.kafka_connect_api.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.time.Duration;

@ConfigMapping(prefix = "kafka-connect-client")
public interface KafkaConnectApiClientConfig {
    @WithName("kafka-connect-api-url")
    String kafkaConnectApiUrl();

    @WithName("request-timeout")
    Duration requestTimeout();
}
