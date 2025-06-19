package org.acme;

import org.acme.model.InsertProductModel;
import org.acme.model.UpdateProductModel;
import org.acme.service.ProductService;
import org.acme.util.Secure;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secure
@WithSession
@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@SecurityRequirement(name = "jwt")
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Product", description = "Returns a product")
    @APIResponse(responseCode = "200", description = "Successful response")
    public Uni<Response> getProductById(@PathParam("id") Long id) {
        return productService.getProduct(id).map(data -> Response.ok().entity(data).build());
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @Operation(summary = "create Product", description = "Returns a product")
    @APIResponse(responseCode = "201", description = "Successful response")
    public Uni<Response> createProduct(@Valid InsertProductModel model) {
        return productService.addProduct(model)
                .map(data -> Response.status(Response.Status.CREATED).entity(data).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get list Product", description = "Returns a list product")
    @APIResponse(responseCode = "200", description = "Successful response")
    public Uni<Response> getAllProducts(
            @QueryParam("name") String name,
            @QueryParam("minPrice") Double minPrice,
            @QueryParam("maxPrice") Double maxPrice) {
        return productService.getAllProducts(name, minPrice, maxPrice).map(data -> Response.ok().entity(data).build());
    }

    @DELETE
    @Path("/{id}")
    @WithTransaction
    @Operation(summary = "Delete Product", description = "Returns a successful message")
    @APIResponse(responseCode = "200", description = "Successful response")
    public Uni<Response> deleteProduct(@PathParam("id") Long id) {
        return productService.deleteProduct(id)
                .map(data -> Response.ok().entity(data).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @Operation(summary = "Update Product", description = "Returns a updated product")
    @APIResponse(responseCode = "200", description = "Successful response")
    public Uni<Response> updateProduct(@PathParam("id") Long id, UpdateProductModel model) {
        return productService.updateProduct(id, model)
                .map(data -> Response.ok().entity(data).build());
    }

}
