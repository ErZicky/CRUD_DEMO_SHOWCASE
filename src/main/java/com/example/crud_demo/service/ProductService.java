package com.example.crud_demo.service;

import com.example.crud_demo.exception.ProductNotFoundException;
import com.example.crud_demo.model.Product;
import com.example.crud_demo.repository.ProductDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service annotation tells spring that this component (service is a specialization of @component) holds the business logic of the application.
 * It acts as an intermediary between the API Controller and the Database Repository.
 * * @Service tells Spring to manage this class as a 'Bean', enabling component scanning and keeping it in the application context
 * and allowing it to be injected into other parts of the application.
 */
@Service
public class ProductService {

    //final is for immutability, meaning that the dependency cannot change after initialization
    private final ProductDatabase db;

    @Autowired //@autowired is optional in the constructor injection if there's only one constructor
    public ProductService(ProductDatabase productDatabase)
    {
        db = productDatabase;
    }


    //CRUD operations fornite da spring

    public List<Product> getAllProducts()
    {
        return db.findAll();
    }



    //optional allow us to use the fluent logic into the controller, otherwise we would need to manually handle the null value with if/else
    public Optional<Product> getProductById(int id) {
        return Optional.of(db.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id))); //here we use our custom ProductNotFoundException
    }


    public Product saveProduct(Product product)
    {
        return db.save(product);
    }

    public Product updateProduct(Product product, int id)
    {
        product.setId(id);
        return db.save(product);
    }

    public void deleteProduct(Product product)
    {
        db.delete(product);
    }

    public void deleteProductById(int id)
    {
        db.deleteById(id);
    }



    public List<Product> getProductByNameLike(String name)
    {
        return db.getProductsByNameContaining(name);
    }
}
