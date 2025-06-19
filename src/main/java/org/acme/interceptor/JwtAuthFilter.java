package org.acme.interceptor;

import java.io.IOException;

import org.acme.model.ResponseModel;
import org.acme.util.Secure;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Secure
@Priority(jakarta.ws.rs.Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {
    private static final Logger LOG = LogManager.getLogger(JwtAuthFilter.class);

    @Inject
    JWTParser jwtParser;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            LOG.warn("Missing or invalid Authorization header");
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ResponseModel<>("Unauthorized", "Missing or invalid token"))
                            .build());
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            LOG.debug(String.format("Valid JWT for user: %s", jwt.getName()));
        } catch (ParseException e) {
            LOG.warn(String.format("Invalid JWT: %s", e.getMessage()));
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ResponseModel<>("Invalid token", "The provided token is invalid"))
                            .build());
        }
    }
}
