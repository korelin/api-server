package ru.tinkoff.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.stream.Stream;

public class ContainerResource implements QuarkusTestResourceLifecycleManager {
    private static Network network = Network.newNetwork();

    public static KafkaContainer kafkaContainer = new KafkaContainer()
        .withNetwork(network);

    public static PostgreSQLContainer<?> postgresContainer =
        new PostgreSQLContainer<>(
            DockerImageName.parse("docker-proxy.artifactory.tcsbank.ru/postgres:15")
                .asCompatibleSubstituteFor("postgres"))
            .withNetwork(network)
            .withNetworkAliases("postgres");

    @Override
    public Map<String, String> start() {
        Startables.deepStart(Stream.of(
                kafkaContainer, postgresContainer))
            .join();
        return null;
    }

    @Override
    public void stop() {

    }
}
