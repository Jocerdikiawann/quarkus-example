package org.acme.repository;

import java.util.Date;

import org.acme.entity.Product;
import org.acme.model.UpdateProductModel;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Uni<Product> getProductById(Long id) {
        return findById(id);
    }

    public Multi<Product> getAllProducts(
            String name,
            Double minPrice,
            Double maxPrice) {
        return find("WHERE name ILIKE ?1 AND price >= ?2 AND price <= ?3",
                name + "%", minPrice, maxPrice)
                .list()
                .onItem()
                .transformToMulti(list -> Multi.createFrom().iterable(list));
    }

    public Uni<Product> createProduct(Product product) {
        product.setCreatedAt(new Date());
        return persist(product);
    }

    public Uni<Product> updateProduct(Long id, UpdateProductModel product) {
        return this.getProductById(id)
                .onItem()
                .ifNotNull()
                .transform(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    return existingProduct;
                })
                .flatMap(this::persist);
    }

    public Uni<Void> deleteProduct(Long id) {
        return this.getProductById(id)
                .onItem()
                .ifNotNull()
                .call(this::delete)
                .replaceWithVoid();
    }

}
