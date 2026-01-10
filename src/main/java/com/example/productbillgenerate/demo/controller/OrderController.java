package com.example.productbillgenerate.demo.controller;


import java.util.Map;

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

   // @PostMapping
   // public ResponseEntity<?>create(@RequestBody OrderRequest request) {
      //  return service.createOrder(request.getCustomerId(), request.getProducts());
   // }  

    @PostMapping("/{customer_id}")
    public ResponseEntity<?>create(@PathVariable Long customer_id, @RequestBody Map<Long,Integer>products) {
        return service.createOrder(customer_id, products);
    }  

  @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        return service.findAllOrders();
    }


    @PutMapping("/{order_id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long order_id) {
        return service.cancelOrder(order_id);
    }

     @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long order_id) {
        return service.deleteOrder(order_id);
    }
}