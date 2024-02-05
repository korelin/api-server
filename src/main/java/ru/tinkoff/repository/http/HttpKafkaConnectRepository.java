package ru.tinkoff.repository.http;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import ru.tinkoff.clients.kafka_connect_api.KafkaConnectApiClient;
import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;
import ru.tinkoff.repository.KafkaConnectRepository;

import java.util.List;

@ApplicationScoped
public class HttpKafkaConnectRepository implements KafkaConnectRepository {
    @Inject
    KafkaConnectApiClient kafkaConnectApiClient;

    @Inject
    Logger logger;

    @Override
    public List<ConnectorPlugin> getPlugins() {
        logger.info("Getting plugins list");
        return kafkaConnectApiClient.listConnectorPlugins();
    }
}
