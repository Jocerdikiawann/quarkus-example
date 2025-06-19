package org.acme.interceptor;

import org.acme.model.ResponseModel;
import org.hibernate.exception.DataException;

import jakarta.persistence.NoResultException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof BadRequestException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseModel<>("Bad Request: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof NoResultException || exception instanceof DataException
                || exception instanceof NotFoundException || exception instanceof NullPointerException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseModel<>("Not Found: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseModel<>("Invalid Argument: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof ForbiddenException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseModel<>("Forbidden: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ResponseModel<>("An error occurred: " + exception.getMessage(), null))
                .type("application/json")
                .build();
    }

}
