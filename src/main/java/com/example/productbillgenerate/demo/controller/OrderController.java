package com.example.productbillgenerate.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.productbillgenerate.demo.service.OrderService;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?>create(@RequestBody OrderRequest request) {
        return service.createOrder(request.getCustomerId(), request.getProducts());
    }   

     @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return service.cancelOrder(id);
    }
}