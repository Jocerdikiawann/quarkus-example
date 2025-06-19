package org.acme.repository;

import org.acme.entity.User;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Uni<User> findByUsername(String username) {
        return find("username", username)
                .firstResult();
    }

    public Uni<User> login(String username, String password) {
        return find("username = ?1 and password = ?2", username, password)
                .firstResult();
    }

    public Uni<Void> register(User user) {
        return persist(user)
                .replaceWithVoid();
    }

}
