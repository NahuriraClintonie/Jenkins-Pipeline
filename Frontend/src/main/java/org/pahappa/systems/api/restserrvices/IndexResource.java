package org.pahappa.systems.api.restserrvices;

import com.google.gson.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class will be used to index the API
 * It will be used to check if the API is up and running
 * This will be the basic structure of your API endpoints
 *
 * @version 1.0
 * @see <a href="https://www.oracle.com/technical-resources/articles/java/jax-rs.html">JAX-RS</a>
 * @see <a href="https://docs.oracle.com/javaee/6/tutorial/doc/giepu.html">Building RESTful Web Services with JAX-RS</a>
 * @since 1.0
 */
@Path("/")
public class IndexResource {

    @GET
    @Path("/index")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Welcome to the API");
        return Response.ok(jsonObject).build();
    }
}
