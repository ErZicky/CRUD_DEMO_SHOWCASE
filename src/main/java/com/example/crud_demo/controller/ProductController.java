package com.example.crud_demo.controller;

import com.example.crud_demo.model.Product;
import com.example.crud_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
RestController combines annotation Controller and ResponseBody. Controller says that this class manage HTTP request (get, post etc..)
responsebody says to spring to automatically convert java object like products to JSON format before sending them to the client
further more it tells spring to instantiate this class as a bean and to keep it in the application context
 */
@RestController
/*
request mapping establish the url base path for all methods of this class, for example if this app run on www.[domain].com all calls to this controller will start with
www.domain.com/api/products/[request]
 */
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService ps;

    @Autowired //constructor dependency injection
    public ProductController(ProductService productService) {
        this.ps = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return ps.getAllProducts();
    }

    //this endpoint will answer to request such as /api/products/1
    @GetMapping ("/{id}")
    //PathVariable fetch the value from the url in the position of the placeholder and injects it into the id variable
    public Optional<Product> getProductById(@PathVariable int id)
    {

        //since in the service and GlobalExceptionHandler we are managing the throw of the exception here there's no need to do the .map and .orElse
        return  ps.getProductById(id);

        //this is what we would do if we were not managing the exception service side (only doable if we use optional<>, otherwise use a simple if else)
        // returns A ResponseEntity containing the JSON of the Product if found with a 200 answer code  or a empty response with a 404 status.
        /*return ps.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());*/

    }

    @GetMapping("/searchLike/{name}")
    public ResponseEntity<List<Product>> searchProductByName(@PathVariable String name)
    {
        List<Product> list = ps.getProductByNameLike(name);

        if(list.isEmpty())
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(list, HttpStatus.OK);

    }


    /**
     * POST endpoint to create a new product.
     * @PostMapping handles HTTP POST requests sent to the base URL (/api/products) since we have not defined anything specific between ().
     * @RequestBody tells Spring to deserialize the incoming JSON from the request body into a Java 'Product' object.
     * @return a response entity with the saved product object (if it succeded) and the HTTP status code .
     * at the moment if something goes wrong, spring will handles the exception and send a error 500 but we can in future create a
     * separate class with @ControllerAdvice which observe all controllers, catch an exeption and decide what to send specifically
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product p)
    {
        Product savedP = ps.saveProduct(p);
        return new ResponseEntity<>(savedP, HttpStatus.CREATED);

    }

    //in the rest world PUT is used for updates and POST for creations
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product p, @PathVariable int id)
    {
        if(ps.getProductById(id).isEmpty())
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(ps.updateProduct(p,id), HttpStatus.OK);
    }

    //in the rest world DELETE is used for deletions request

    //passing a request body for deletion is RARE and not the standard
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestBody Product p)
    {
        if(ps.getProductById(p.getId()).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ps.deleteProduct(p);
        //we could return just a simple status OK on deletion but the standard is to return NO CONTENT (204) which is like saying to the client
        // "I did what you asked, I have nothing to return because there's nothing anymore
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }


    //these two delete request accomplish the same thing, but Spring can distinguish among them becasue this one uses a different URL and require just an id, while the other doesn't have a specific url
    // and require a request body
    //this deletaion method with just a variable is the standard
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable int id)
    {


        //since the exeption for this method is handled service side whe just need to call it like this:
        ps.getProductById(id);

        //otherwise we would need to do something like this:
       /* if(ps.getProductById(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/

        ps.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
