package ru.tinkoff.clients;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.clients.kafka_connect_api.KafkaConnectApiClient;
import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;
import ru.tinkoff.wiremock.WireMockStubs;

import java.util.List;

@QuarkusTest
@QuarkusTestResource(value = WireMockStubs.class, restrictToAnnotatedClass = true)
public class HttpKafkaConnectApiClientTest {
    @Inject
    KafkaConnectApiClient connectApiClient;
    @Test
    public void successfullyGetConnectorsPlugins() {
        var expected = List.of(
            new ConnectorPlugin("io.confluent.connect.jdbc.JdbcSinkConnector", "sink", "10.6.0"),
            new ConnectorPlugin("io.confluent.connect.jdbc.JdbcSourceConnector", "source", "10.6.0")
        );
        var plugins = connectApiClient.listConnectorPlugins();
        Assertions.assertEquals(expected, plugins);
    }

}
