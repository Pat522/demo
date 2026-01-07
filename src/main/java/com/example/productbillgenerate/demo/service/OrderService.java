package com.example.productbillgenerate.demo.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productbillgenerate.demo.model.Customer;
import com.example.productbillgenerate.demo.model.Order;
import com.example.productbillgenerate.demo.model.OrderItem;
import com.example.productbillgenerate.demo.model.Product;
import com.example.productbillgenerate.demo.repo.CustomerRepository;
import com.example.productbillgenerate.demo.repo.OrderItemRepository;
import com.example.productbillgenerate.demo.repo.OrderRepository;
import com.example.productbillgenerate.demo.repo.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(OrderRepository orderRepo,
                        CustomerRepository customerRepo,
                        ProductRepository productRepo,
                        OrderItemRepository orderItemRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public ResponseEntity<?> createOrder(Long customerId, Map<Long, Integer> products) {

        Customer customer = customerRepo.findById(customerId).orElse(null);

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productRepo.findById(entry.getKey()).orElse(null);

             if (!"Active".equalsIgnoreCase(product.getProductStatus())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Product is discontinued and can not order");
            }

            if (product.getStockQuantity() < entry.getValue()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient stock for product: " + product.getProductName());
            }
        }


        Order order = new Order();
        order.setId(order.getId());
        order.setCustomer(customer);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setStatus(Order.OrderStatus.CREATED);
        order = orderRepo.save(order);

    List<Map<String, Object>> productList = new ArrayList<>();
    double totalAmount = 0;

    for (Long productId : products.keySet()) 
    {

    Integer quantity = products.get(productId);

    Product product = productRepo.findById(productId).get();
    product.setStockQuantity(product.getStockQuantity() - quantity);
    productRepo.save(product);


    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(product);
    orderItem.setQuantity(quantity);
    orderItem.setPrice(product.getPrice());
    orderItemRepo.save(orderItem);

    double itemTotal = product.getPrice() * quantity;
    totalAmount =totalAmount+ itemTotal;

    Map<String, Object> productMap = new HashMap<>();
    productMap.put("productId", product.getId());
    productMap.put("productName", product.getProductName());
    productMap.put("wattage", product.getWattage());
    productMap.put("category", product.getCategory());
    productMap.put("price", product.getPrice());
    productMap.put("quantity", quantity);
    productMap.put("status", product.getProductStatus());

    productList.add(productMap);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("orderNumber", order.getOrderNumber());
    response.put("orderDate", order.getOrderDate());
    response.put("status", order.getStatus());
    response.put("Id",order.getId());
    response.put("totalAmount", totalAmount);
    response.put("products", productList);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<?> cancelOrder(Long orderId) {

        Order order = orderRepo.findById(orderId).orElse(null);

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Order is Cancel Already");
        }


      List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
      for (OrderItem orderItem : orderItems) {
       Product product = orderItem.getProduct();
       product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
       productRepo.save(product);
   }
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepo.save(order);

        return ResponseEntity.ok("Order cancelled successfully. Stock restored.");
    }
}