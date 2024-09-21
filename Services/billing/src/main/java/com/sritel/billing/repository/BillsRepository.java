package com.sritel.billing.repository;

import com.sritel.billing.entity.Bills;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BillsRepository extends MongoRepository<Bills, String> {
    List<Bills> findByUserId(String userId);

    @Query("{ 'userId': ?0, 'billingDate': { $gte: ?1, $lt: ?2 } }")
    List<Bills> findBillsForCurrentMonth(String userId, LocalDate startOfMonth, LocalDate startOfNextMonth);
}
