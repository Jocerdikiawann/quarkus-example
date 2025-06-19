package org.acme.repository;

import java.util.Date;

import org.acme.entity.Product;
import org.acme.model.UpdateProductModel;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
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
        String queryBuilder = "";
        Parameters param = new Parameters();
        if (name != null && !name.isEmpty()) {
            queryBuilder += "WHERE name ILIKE :name ";
            param.and("name", name + "%");
        }
        if (minPrice != null) {
            queryBuilder += (queryBuilder.isEmpty() ? "WHERE " : "AND ") + "price >= :minPrice";
            param.and("minPrice", minPrice);
        }

        if (maxPrice != null) {
            queryBuilder += (queryBuilder.isEmpty() ? "WHERE " : "AND ") + "price <= :maxPrice";
            param.and("maxPrice", maxPrice);
        }

        return find(queryBuilder, param)
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
