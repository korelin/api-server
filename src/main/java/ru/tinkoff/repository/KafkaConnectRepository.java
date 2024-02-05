package ru.tinkoff.repository;

import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;

import java.util.List;

public interface KafkaConnectRepository {
    List<ConnectorPlugin> getPlugins();
}
