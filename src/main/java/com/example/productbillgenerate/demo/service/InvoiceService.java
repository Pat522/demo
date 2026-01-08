package com.example.productbillgenerate.demo.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.productbillgenerate.demo.model.Invoice;
import com.example.productbillgenerate.demo.model.Order;
import com.example.productbillgenerate.demo.model.OrderItem;
import com.example.productbillgenerate.demo.repo.InvoiceRepository;
import com.example.productbillgenerate.demo.repo.OrderItemRepository;
import com.example.productbillgenerate.demo.repo.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class InvoiceService {

    private static final double GST_RATE = 0.18;

    private final InvoiceRepository invoiceRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public InvoiceService(InvoiceRepository invoiceRepo,
                          OrderRepository orderRepo,
                          OrderItemRepository orderItemRepo) {
        this.invoiceRepo = invoiceRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public ResponseEntity<?> generateInvoiceByOrderId(Long orderId) 
    {
       Order  order = orderRepo.findById(orderId).orElse(null);

        // Prevent duplicate invoice
        if (invoiceRepo.findById(orderId).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invoice already generated for this order");
        }

        //Fetch Order Item
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
         if (items.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No order items found for this order");
        }

       double totalAmount = 0;

       for (OrderItem item : items) {
       totalAmount = totalAmount+(item.getPrice() * item.getQuantity());
    }

    double gstAmount = totalAmount * 0.18;
    double grandTotal = totalAmount + gstAmount;


        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(System.currentTimeMillis());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setTotalAmount(grandTotal);
        invoice.setGstAmount(gstAmount);
        invoice.setPaymentStatus(Invoice.Status.PAID);

        invoiceRepo.save(invoice);

        Map<String, Object> response = new HashMap<>();
        response.put("invoiceNumber", invoice.getInvoiceNumber());
        response.put("invoiceDate", invoice.getInvoiceDate());
        response.put("orderNumber", order.getOrderNumber());
        response.put("totalAmount", totalAmount);
        response.put("gstAmount", gstAmount);
        response.put("grandTotal", grandTotal);
        response.put("paymentStatus", invoice.getPaymentStatus());

        return ResponseEntity.ok(response);
    }




  

}
