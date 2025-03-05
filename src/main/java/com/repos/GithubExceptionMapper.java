package com.repos;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class GithubExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {

    @Override
    public Response toResponse(ClientWebApplicationException exception) {
        int status = exception.getResponse().getStatus();

        if (status == 404) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "User not found on GitHub");

            return Response.status(404).entity(errorResponse).build();
        }

        Map<String, Object> genericError = new LinkedHashMap<>();
        genericError.put("status", status);
        genericError.put("message", "Unexpected error occurred");

        return Response.status(status).entity(genericError).build();
    }
}