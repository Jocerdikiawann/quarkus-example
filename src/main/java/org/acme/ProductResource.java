package org.acme;

import org.acme.model.InsertProductModel;
import org.acme.model.UpdateProductModel;
import org.acme.service.ProductService;
import org.acme.util.Secure;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProductById(@PathParam("id") Long id) {
        return productService.getProduct(id).map(data -> Response.ok().entity(data).build());
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createProduct(InsertProductModel model) {
        return productService.addProduct(model)
                .map(data -> Response.status(Response.Status.CREATED).entity(data).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllProducts(
            @QueryParam("name") String name,
            @QueryParam("minPrice") Double minPrice,
            @QueryParam("maxPrice") Double maxPrice) {
        return productService.getAllProducts(name, minPrice, maxPrice).map(data -> Response.ok().entity(data).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteProduct(@PathParam("id") Long id) {
        return productService.deleteProduct(id)
                .map(data -> Response.ok().entity(data).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateProduct(@PathParam("id") Long id, UpdateProductModel model) {
        return productService.updateProduct(id, model)
                .map(data -> Response.ok().entity(data).build());
    }

}
