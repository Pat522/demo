package com.example.productbillgenerate.demo.controller;

import java.util.Map;

public class OrderRequest {

    private Long customerId;
    private Map<Long, Integer> products;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }
}

