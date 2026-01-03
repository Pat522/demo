package com.example.productbillgenerate.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer wattage;
    private Category  category;

    public enum Category {
    Indoor,
    Outdoor,

}
    private Double price;   
    private Integer stockQuantity;
    private Status status;

    public enum Status {
    Active,
    Discontinued
    }

} 