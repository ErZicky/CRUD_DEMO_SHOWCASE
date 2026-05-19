package com.example.crud_demo.service;

import com.example.crud_demo.exception.ProductNotFoundException;
import com.example.crud_demo.model.Product;
import com.example.crud_demo.repository.ProductDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    /*
     * returns the product if it is found, otherwise it throws a ProductNotFoundException
     * which is intercepted by the GlobalExceptionHandler and converted into a 404 response.
     * we could have used an Optional<Product> here, but since this method never actually returns an empty
     * optional (it always either contains a value or throws), wrapping the result in Optional was misleading
     * for the caller, so we now return the Product directly.
     */
    public Product getProductById(int id) {
        return db.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id)); //here we use our custom ProductNotFoundException
    }


    /*
     * @Transactional only on writes: it opens a read-write transaction that commits if the method returns normally
     * or rolls back if a runtime exception is thrown halfway through. read methods don't need it here because every
     * one of them is a single JpaRepository call and Spring Data already wraps those in a read-only transaction internally,
     * so adding a class-level @Transactional(readOnly = true) would do nothing in this repo.
     * the writes get @Transactional because as soon as we evolve them into multi-step business operations
     * we want the whole sequence to be atomic
     */
    @Transactional
    public Product saveProduct(Product product)
    {
        return db.save(product);
    }

    @Transactional
    public Product updateProduct(Product product, int id)
    {
        product.setId(id);
        return db.save(product);
    }

    @Transactional
    public void deleteProduct(Product product)
    {
        db.delete(product);
    }

    @Transactional
    public void deleteProductById(int id)
    {
        db.deleteById(id);
    }



    public List<Product> getProductByNameLike(String name)
    {
        return db.getProductsByNameContaining(name);
    }
}
