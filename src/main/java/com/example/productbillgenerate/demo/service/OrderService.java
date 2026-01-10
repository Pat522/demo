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
import com.example.productbillgenerate.demo.repo.InvoiceRepository;
import com.example.productbillgenerate.demo.repo.OrderItemRepository;
import com.example.productbillgenerate.demo.repo.OrderRepository;
import com.example.productbillgenerate.demo.repo.ProductRepository;

@Service
public class OrderService {

    private final InvoiceRepository invoiceRepo;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final OrderItemRepository orderItemRepo;



    public OrderService(OrderRepository orderRepo,
                        CustomerRepository customerRepo,
                        ProductRepository productRepo,
                        OrderItemRepository orderItemRepo,InvoiceRepository invoiceRepo) 
    {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
        this.orderItemRepo = orderItemRepo;
        this.invoiceRepo=invoiceRepo;
    }

   @Transactional
    public ResponseEntity<?> createOrder(Long customer_id, Map<Long, Integer> products) 
    {

    Customer customer = customerRepo.findById(customer_id)
                        .orElse(null);
     
    List<Map<String, Object>> productList = new ArrayList<>();
    double totalAmount = 0;
   
    for (Long productId : products.keySet()) 
    {
           Product product = productRepo.findById(productId).get();
            Integer orderQuantity = products.get(productId);

             if (!"Active".equalsIgnoreCase(product.getProductStatus())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Product is discontinued and can not order");
            }
            if (product.getStockQuantity() < orderQuantity) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient stock for product: " + product.getProductName());
            }
    }


        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(Order.Status.CREATED);
        order = orderRepo.save(order);

   
    for(Long productId : products.keySet()) {
    Product product = productRepo.findById(productId).get();
    Integer quantity = products.get(productId);
    double price=product.getPrice();

    product.setStockQuantity(product.getStockQuantity() - quantity);
    productRepo.save(product);


    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(product);
    orderItem.setQuantity(quantity);
    orderItem.setPrice(price);
    orderItemRepo.save(orderItem);

    double itemTotal = product.getPrice() * quantity;
    totalAmount =totalAmount + itemTotal;

    Map<String, Object> productMap = new LinkedHashMap<>();
    productMap.put("productId", product.getId());
    productMap.put("productName", product.getProductName());
    productMap.put("wattage", product.getWattage());
    productMap.put("category", product.getCategory());
    productMap.put("price", product.getPrice());
    productMap.put("quantity", quantity);
    productMap.put("status", product.getProductStatus());

    productList.add(productMap);
    }

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("orderId",order.getId());
    response.put("orderNumber", order.getOrderNumber());
    response.put("orderDate", order.getOrderDate());
    response.put("totalAmount", totalAmount);  
    response.put("status", order.getOrderStatus());
    response.put("products", productList);

    return ResponseEntity.ok().body(response);
}

 @Transactional
    public ResponseEntity<?> findAllOrders() {
       
    List<Order> orders = orderRepo.findAll();

    List<Map<String, Object>> orderList = new ArrayList<>();

    for (Order order : orders) {

        List<OrderItem> orderItems = orderItemRepo.findByOrder(order);

        List<Map<String, Object>> productList = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItem item : orderItems) {

            Product product = item.getProduct();
            double itemTotal = item.getPrice() * item.getQuantity();
            totalAmount = totalAmount + itemTotal;

            Map<String, Object> productMap = new LinkedHashMap<>();
            productMap.put("productId", product.getId());
            productMap.put("productName", product.getProductName());
            productMap.put("wattage", product.getWattage());
            productMap.put("category", product.getCategory());
            productMap.put("price", item.getPrice());
            productMap.put("quantity", item.getQuantity());
            productMap.put("status", product.getProductStatus());

            productList.add(productMap);
        }

        Map<String, Object> orderMap = new LinkedHashMap<>();
        orderMap.put("orderId", order.getId());
        orderMap.put("orderNumber", order.getOrderNumber());
        orderMap.put("orderDate", order.getOrderDate());
        orderMap.put("status", order.getOrderStatus());
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("products", productList);

        orderList.add(orderMap);
    }

    return ResponseEntity.ok(orderList);
}  

    @Transactional
    public ResponseEntity<?> cancelOrder(Long order_id) 
    {
    Order order = orderRepo.findById(order_id).orElse(null);
    if (order == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Order not found for order id: " + order_id);
                       }
  

    if (order.getOrderStatus() == Order.Status.CANCELLED) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Order is already cancelled");
                       }


    if (invoiceRepo.findById(order_id).isPresent()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot cancel the order because invoice is already generated for this order");
                       }


    List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
    for (OrderItem orderItem : orderItems) {
    Product product = orderItem.getProduct();
    product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
    productRepo.save(product);
                        }


    order.setOrderStatus(Order.Status.CANCELLED);
    orderRepo.save(order);

    return ResponseEntity.ok("Order cancelled successfully. Stock restored.");
    }

    public ResponseEntity<?> deleteOrder(Long order_id) 
    {

    Order order = orderRepo.findById(order_id).orElse(null);
    if(order==null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Order already Deleted:" + order_id);
                   }

    orderItemRepo.deleteById(order_id);
    orderRepo.delete(order);

    return ResponseEntity.ok("Order deleted successfully");
    } 
}
    
