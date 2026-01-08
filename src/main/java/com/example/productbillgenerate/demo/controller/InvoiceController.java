package com.example.productbillgenerate.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productbillgenerate.demo.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
      private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Generate Invoice
   @PostMapping("/generate/{orderId}")
    public ResponseEntity<?> generateInvoice(@PathVariable("orderId") Long orderId) 
    {
    return invoiceService.generateInvoiceByOrderId(orderId);
    }


}
