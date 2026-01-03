package com.example.productbillgenerate.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.productbillgenerate.demo.model.Customer;
import com.example.productbillgenerate.demo.repo.CustomerRepository;

import tools.jackson.databind.ObjectMapper;


@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    @Autowired
    private ObjectMapper objectMapper;


    public ResponseEntity<Customer> addCustomer(Customer customer) {
      if(customer.getEmail()==null || customer.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if(customer.getMobile()==null || customer.getMobile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }   
        Customer savedCustomer = repo.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = repo.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    public ResponseEntity<Customer> getCustomerById(Long id) {
        Customer customer = repo.findById(id).orElseThrow() ;
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Customer> updateCustomer(Long id, Customer updatedCustomer) {
        Customer customer = repo.findById(id).orElseThrow();
        if (customer != null) {
            customer.setCustomerName(updatedCustomer.getCustomerName());
            customer.setContactPerson(updatedCustomer.getContactPerson());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setMobile(updatedCustomer.getMobile());
            customer.setGstNumber(updatedCustomer.getGstNumber());
            customer.setAddress(updatedCustomer.getAddress());
            customer.setCity(updatedCustomer.getCity());
            customer.setState(updatedCustomer.getState());
            Customer saved = repo.save(customer);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Customer> patchCustomer(Long id, Map<String, Object> updates) {
    Customer customer = repo.findById(id).orElseThrow();

    objectMapper.updateValue(customer, updates);

    return ResponseEntity.ok(customer);
}

public ResponseEntity<String> deleteCustomerById(Long id) {
    return repo.existsById(id)
            ? ResponseEntity.ok("Customer deleted successfully.")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
}
}


