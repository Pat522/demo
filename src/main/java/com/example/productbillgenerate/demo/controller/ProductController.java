package com.example.productbillgenerate.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productbillgenerate.demo.model.Product;
import com.example.productbillgenerate.demo.service.ProductService;

@RestController
@RequestMapping("/api")    
public class ProductController {

    
   @Autowired
   private ProductService service;

   @PostMapping("/products")
   public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        return service.addProduct(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll()
    {
        return service.getAllProducts();
    }   

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/products/{id}/{quantity}")
    public ResponseEntity<Product> updateStockQuantity(@PathVariable Long id,
                                                        @PathVariable Integer quantity)
    {
        return service.updateStockQuantity(id, quantity);
    }   

   @DeleteMapping("/products/{id}")
   public ResponseEntity<String> delete(@PathVariable Long id) {
    return service.deleteById(id);
    }
} 