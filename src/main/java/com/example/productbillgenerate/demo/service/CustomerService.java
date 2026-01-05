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

    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = repo.findById(id).orElseThrow();

        if(isAnyFieldNullOrEmpty(customer)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }   

 
        Customer updatedCustomer = repo.save(existingCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    private boolean isAnyFieldNullOrEmpty(Customer customer) {
        return customer.getEmail() == null || customer.getEmail().isEmpty() ||
               customer.getMobile() == null || customer.getMobile().isEmpty();
    }

    public ResponseEntity<Customer> patchCustomer(Long id, Map<String, Object> updates) {
    Customer customer = repo.findById(id).orElseThrow();

    objectMapper.updateValue(customer, updates);

    return ResponseEntity.ok(customer);
}

    public ResponseEntity<String> deleteCustomerById(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found with id: " + id);
        }

        repo.deleteById(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }

}


