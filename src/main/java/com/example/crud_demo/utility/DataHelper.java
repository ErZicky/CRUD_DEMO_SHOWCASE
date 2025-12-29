package com.example.crud_demo.utility;

import com.example.crud_demo.model.Product;
import com.example.crud_demo.repository.ProductDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataHelper implements CommandLineRunner {

    private final ProductDatabase productDatabase;


    @Autowired
    public DataHelper(ProductDatabase productDatabase) {
        this.productDatabase = productDatabase;
    }

    @Override
    public void run(String... args) throws Exception {

       Product product1 = new Product("test1", "description", 2.0);

       productDatabase.save(product1);


    }
}
