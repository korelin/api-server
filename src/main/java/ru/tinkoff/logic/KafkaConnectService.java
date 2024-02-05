package ru.tinkoff.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;
import ru.tinkoff.repository.KafkaConnectRepository;

import java.util.List;
import java.util.stream.Collectors;
@ApplicationScoped
public class KafkaConnectService {
    @Inject
    KafkaConnectRepository kafkaConnectRepository;

    public List<String> getConnectors() {
        return kafkaConnectRepository.getPlugins().stream().map(ConnectorPlugin::className).collect(Collectors.toList());
    }
}
