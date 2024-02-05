package ru.tinkoff.clients.kafka_connect_api;

import io.smallrye.mutiny.Uni;
import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;

import java.util.List;

public interface KafkaConnectApiClient {
    List<ConnectorPlugin> listConnectorPlugins();
}
