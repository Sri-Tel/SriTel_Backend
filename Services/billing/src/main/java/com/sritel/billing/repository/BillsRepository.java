package com.sritel.billing.repository;

import com.sritel.billing.entity.Bills;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillsRepository extends MongoRepository<Bills, String> {
    List<Bills> findByUserId(String userId);
}
