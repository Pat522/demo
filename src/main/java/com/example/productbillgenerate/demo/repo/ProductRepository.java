package com.example.productbillgenerate.demo.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productbillgenerate.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}

