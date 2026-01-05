package com.example.productbillgenerate.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productbillgenerate.demo.model.Product;
import com.example.productbillgenerate.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")    
public class ProductController {

    
   @Autowired
   private ProductService service;

   @PostMapping
   public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        return service.addProduct(product);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll()
    {
        return service.getAllProducts();
    }   

    @GetMapping("/all/filter/InStock")
    public ResponseEntity<List<Product>> getProductsByStockQuantity() {
        return service.getProductsByStockQuantity();
    }

    @GetMapping("/all/filter/ByPrice")
    public ResponseEntity<List<Product>> getProductByPrice() {
        return service.getProductByPrice();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @PatchMapping("/{id}/{quantity}")
    public ResponseEntity<Product> updateStockQuantity(@PathVariable Long id,
                                                        @PathVariable Integer quantity)
    {
        return service.updateStockQuantity(id, quantity);
    }   

   @DeleteMapping("/{id}")
   public ResponseEntity<String> delete(@PathVariable Long id) {
    return service.deleteProductById(id);
    }
} 