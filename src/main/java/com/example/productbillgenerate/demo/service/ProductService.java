package com.example.productbillgenerate.demo.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.productbillgenerate.demo.model.Product;
import com.example.productbillgenerate.demo.repo.ProductRepository;

@Service
public class ProductService {

    @Autowired
    public ProductRepository repo;

    public ResponseEntity<Product> addProduct(Product product) {
        if(product.getProductName()==null || product.getProductName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }       

        if (product.getPrice() == null || product.getPrice() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if(product.getStockQuantity()==null || product.getStockQuantity()<=0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Product savedProduct=repo.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

    }


    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> savedProduct=repo.findAll();
       return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
    }

    public ResponseEntity<Product> getById(Long id) {
       Product product = repo.findById(id).orElseThrow();
        if(product!=null)
        {
            return ResponseEntity.ok(product);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   public ResponseEntity<Product> updateStockQuantity(Long id, Integer quantity) {
    Product savedProduct = repo.findById(id)
            .orElseThrow();
            if(quantity==0 || quantity<0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            savedProduct.setStockQuantity(quantity);
            repo.save(savedProduct);
        return ResponseEntity.ok(savedProduct);
    }

   public ResponseEntity<String> deleteById(Long id) {
      return repo.existsById(id)
            ? ResponseEntity.ok("Product deleted successfully.")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
  
   }
} 