package org.acme.service;

import org.acme.entity.User;
import org.acme.model.LoginModel;
import org.acme.model.RegisterModel;
import org.acme.model.ResponseModel;
import org.acme.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class AuthenticationService {
    private static final Logger LOG = LogManager.getLogger(AuthenticationService.class);

    @Inject
    UserRepository userRepo;

    @Inject
    @ConfigProperty(name = "app.api-key")
    String apiKey;

    @Inject
    @ConfigProperty(name = "app.jwt.issuer")
    String jwtIssuer;

    @Inject
    @ConfigProperty(name = "app.jwt.expiration")
    Long jwtExpiration;

    public Uni<ResponseModel<String>> login(LoginModel model) {
        if (!this.validateApiKey(apiKey))
            throw new ForbiddenException("Invalid API key");
        return userRepo.findByUsername(model.getUsername())
                .onItem().ifNull().failWith(() -> new ForbiddenException("User not found"))
                .onItem().transform(user -> {
                    if (!BcryptUtil.matches(model.getPassword(), user.getPassword())) {
                        LOG.warn("Invalid password for user: " + model.getUsername());
                        throw new ForbiddenException("Invalid password");
                    }
                    LOG.info("User logged in successfully: " + user.getUsername());
                    return new ResponseModel<>("Login successful", generateJWT(user));
                });
    }

    public Uni<ResponseModel<String>> register(RegisterModel model) {
        if (!this.validateApiKey(apiKey))
            throw new ForbiddenException("Invalid API key");

        return userRepo.findByUsername(model.getUsername())
                .onItem().ifNotNull().failWith(() -> new ForbiddenException("User already exists"))
                .onItem().transformToUni(user -> {
                    User newUser = new User();
                    newUser.setUsername(model.getUsername());
                    newUser.setPassword(BcryptUtil.bcryptHash(model.getPassword()));

                    return userRepo.register(newUser)
                            .replaceWith(new ResponseModel<>("Registration successful", generateJWT(newUser)));
                });
    }

    private boolean validateApiKey(String providedApiKey) {
        if (!apiKey.equals(providedApiKey)) {
            LOG.warn("Invalid API key provided");
            throw new ForbiddenException("Invalid API key");
        }
        return true;
    }

    private String generateJWT(User user) {
        LOG.info("Generating JWT for user: " + user.getUsername());
        return Jwt.issuer(jwtIssuer).upn(user.getUsername())
                .expiresIn(jwtExpiration)
                .sign();
    }
}
