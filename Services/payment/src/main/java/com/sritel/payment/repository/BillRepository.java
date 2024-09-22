package com.sritel.payment.repository;

import com.sritel.payment.entity.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillRepository extends MongoRepository<Bill, String> {
}
