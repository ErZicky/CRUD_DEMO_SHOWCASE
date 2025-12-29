package com.example.crud_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    //similar to SQL AUTO_INCREMENT, increase id on insert in db
    //sequence would use a db object like a sequence to ask for next id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int Id;
    private String name;
    private String description;
    private double price;

    //empty constructor for hibernate
    public Product()
    {}

    public Product(String name, String description, double price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void setName(String name_)
    {
        this.name=name_;
    }

    public void setPrice(double price_)
    {
        this.price=price_;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
