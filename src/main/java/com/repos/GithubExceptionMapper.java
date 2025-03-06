package com.repos;

import com.repos.dto.ErrorDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class GithubExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {

    @Override
    public Response toResponse(ClientWebApplicationException exception) {
        int status = exception.getResponse().getStatus();

        if (status == 404) {
            ErrorDTO errorResponse = new ErrorDTO(404, "User not found on GitHub");
            return Response.status(404).entity(errorResponse).build();
        }

        ErrorDTO genericError = new ErrorDTO(status, "Unexpected error occurred");
        return Response.status(status).entity(genericError).build();
    }
}