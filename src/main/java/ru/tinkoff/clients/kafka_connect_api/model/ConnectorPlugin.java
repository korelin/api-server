package ru.tinkoff.clients.kafka_connect_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConnectorPlugin(
    @JsonProperty("class")
    String className,
    String type,
    String version
    ) {
}
