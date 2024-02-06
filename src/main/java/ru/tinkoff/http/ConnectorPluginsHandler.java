package ru.tinkoff.http;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.tinkoff.logic.KafkaConnectService;

import java.util.List;

@Path("/connector-plugins")
public class ConnectorPluginsHandler {
    @Inject
    KafkaConnectService kafkaConnectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list() {
        return kafkaConnectService.getConnectorPlugins();
    }
}
