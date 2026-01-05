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

       
        if(product.getStockQuantity()==null || product.getStockQuantity()<=0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Product savedProduct=repo.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }


    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products=repo.findAll();
        return ResponseEntity.ok(products);
        
    }
        
      public ResponseEntity<List<Product>> getProductsByStockQuantity() {
      List<Product> products = repo.findAll().stream()
            .filter(product -> product.getStockQuantity() != null && product.getStockQuantity() > 0)
            .toList();
          return ResponseEntity.ok(products);
          
   }

    public ResponseEntity<List<Product>> getProductByPrice() {
    List<Product> products = repo.findAll().stream()
            .filter(product -> product.getPrice() != null && product.getPrice() > 0)
            .toList();
        return ResponseEntity.ok(products); 
       
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

   public ResponseEntity<Product> updateProduct(Long id, Product product) {
    Product products = repo.findById(id)
            .orElseThrow();
            if(products==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            else {
            products.setProductName(product.getProductName());
            products.setWattage(product.getWattage());
            products.setCategory(product.getCategory());
            products.setPrice(product.getPrice());
            products.setStockQuantity(product.getStockQuantity());
            products.setProductStatus(product.getProductStatus());
            repo.save(products);
            return ResponseEntity.ok(products);
            }
    }
   
   public ResponseEntity<Product> updateStockQuantity(Long id, Integer quantity) {
    Product product = repo.findById(id).orElseThrow();
    if (product != null) {
        product.setStockQuantity(quantity);
        Product savedProduct = repo.save(product);
        return ResponseEntity.ok(savedProduct);
              } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
           }
    } 

    public ResponseEntity<String> deleteProductById(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found with id: " + id);
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


    
}
      
      
      
      
   
    
  

