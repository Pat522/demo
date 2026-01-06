package com.example.productbillgenerate.demo.model;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private LocalDate orderDate;
    private double totalAmount;
    private Integer ordeQuantity;


    @ManyToOne
    private Customer customer;

    @OneToMany
    private List<OrderItem> items;

    private OrderStatus status;

       public enum OrderStatus {
        CREATED,
DISPATCHED,        
CANCELLED
    }


}


