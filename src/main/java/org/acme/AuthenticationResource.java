package org.acme;

import org.acme.model.LoginModel;
import org.acme.model.RegisterModel;
import org.acme.service.AuthenticationService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
@WithSession
public class AuthenticationResource {
    @Inject
    AuthenticationService authService;

    @Path("/login")
    @POST
    public Uni<Response> login(@Valid LoginModel loginModel) {
        return authService.login(loginModel)
                .map(user -> Response.ok().entity(user).build());
    }

    @Path("/register")
    @POST
    @WithTransaction
    public Uni<Response> register(@Valid RegisterModel registerModel) {
        return authService.register(registerModel)
                .map(user -> Response.status(Response.Status.CREATED).entity(user).build());
    }
}
