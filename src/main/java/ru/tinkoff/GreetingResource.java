package ru.tinkoff;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public String hello(
        @PathParam("name") String name
        ) {
            return String.format("Hello, %s!", name);
        }
}
