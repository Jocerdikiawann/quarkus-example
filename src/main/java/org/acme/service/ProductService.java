package org.acme.service;

import java.util.List;

import org.acme.entity.Product;
import org.acme.model.InsertProductModel;
import org.acme.model.ResponseModel;
import org.acme.model.UpdateProductModel;
import org.acme.repository.ProductRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {
    private static final Logger LOG = LogManager.getLogger(ProductService.class);

    @Inject
    ProductRepository productRepo;

    public Uni<ResponseModel<Product>> addProduct(InsertProductModel model) {
        return productRepo.createProduct(model.toProduct())
                .map(data -> {
                    LOG.info("Product created successfully: " + data.getId());
                    return new ResponseModel<>("success created", data);
                });
    }

    public Uni<ResponseModel<Product>> updateProduct(Long id, UpdateProductModel model) {
        return productRepo.updateProduct(id, model)
                .map(data -> {
                    LOG.info("Product updated successfully: " + data.getId());
                    return new ResponseModel<>("success updated", data);
                });
    }

    public Uni<ResponseModel<String>> deleteProduct(Long id) {
        return productRepo.deleteProduct(id)
                .map(data -> {
                    LOG.info("Product deleted successfully: " + id);
                    return new ResponseModel<>("success deleted", null);
                });
    }

    public Uni<ResponseModel<Product>> getProduct(Long id) {
        return productRepo.getProductById(id)
                .map(data -> {
                    LOG.info("Product retrieved successfully: " + data.getId());
                    return new ResponseModel<>("success retrieved", data);
                });
    }

    public Uni<ResponseModel<List<Product>>> getAllProducts(String name, Double minPrice, Double maxPrice) {
        return productRepo.getAllProducts(name, minPrice, maxPrice)
                .collect().asList()
                .map(data -> {
                    LOG.info("Products retrieved successfully");
                    return new ResponseModel<>("success retrieved", data);
                });
    }

}
