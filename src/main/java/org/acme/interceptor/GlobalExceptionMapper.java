package org.acme.interceptor;

import org.acme.model.ResponseModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
    private static final Logger LOG = LogManager.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof BadRequestException) {
            LOG.error("Bad Request: " + exception.getMessage(), exception);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseModel<>("Bad Request: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof NoResultException || exception instanceof DataException
                || exception instanceof NotFoundException || exception instanceof NullPointerException) {
            LOG.error("Not Found: " + exception.getMessage(), exception);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseModel<>("Not Found: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof IllegalArgumentException) {
            LOG.error("Invalid Argument: " + exception.getMessage(), exception);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseModel<>("Invalid Argument: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        if (exception instanceof ForbiddenException) {
            LOG.error("Forbidden: " + exception.getMessage(), exception);
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseModel<>("Forbidden: " + exception.getMessage(), null))
                    .type("application/json")
                    .build();
        }

        LOG.error("Internal Server Error: " + exception.getMessage(), exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ResponseModel<>("An error occurred: " + exception.getMessage(), null))
                .type("application/json")
                .build();
    }

}
