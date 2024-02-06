package ru.tinkoff.clients;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.resources.ContainerResource;

@QuarkusTest
@QuarkusTestResource(value = ContainerResource.class, restrictToAnnotatedClass = true)
public class StubKafkaConnectApiClientTest {
    @Test
    public void successfullyGetConnectorsPlugins() {
        Assertions.assertEquals("PLAINTEXT://localhost:", ContainerResource.kafkaContainer.getBootstrapServers().substring(0, 22));
        Assertions.assertEquals("test", ContainerResource.postgresContainer.getDatabaseName());
    }
}
