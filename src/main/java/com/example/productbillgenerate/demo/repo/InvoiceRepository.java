package com.example.productbillgenerate.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productbillgenerate.demo.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {



}
