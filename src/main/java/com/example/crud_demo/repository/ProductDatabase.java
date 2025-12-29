package com.example.crud_demo.repository;

import com.example.crud_demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface acts as a Data Access Object (DAO) for the Product entity.
 * It provides standard CRUD operations out-of-the-box without manual implementation.
 * * Using an interface ensures Decoupling: it separates the business logic from
 * the specific database technology, making the code more modular,
 * easier to test with mocks, and simpler to maintain if the storage layer changes.
 */
@Repository //annotation not strictly necessary (spring would have recognized this interface as repository nontheless but best practice for clarity)
public interface ProductDatabase extends JpaRepository<Product, Integer> {
    List<Product> getProductsByNameContaining(String name);


    // Spring Data JPA generates the implementation at runtime.

    //basically if we will create a List<Product> findByName(String name); spring will read the method and generate it's implementation and query to get the products
}