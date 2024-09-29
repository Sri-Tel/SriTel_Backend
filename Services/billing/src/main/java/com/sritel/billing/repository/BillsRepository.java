package com.sritel.billing.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sritel.billing.entity.Bills;

public interface BillsRepository extends MongoRepository<Bills, String>, CustomBillsRepository {
    @Query("{'userId' : ?0 }")
    List<Bills> findAllByUserId(String userId);

    @Query("{ 'userId': ?0, 'billingDate': { $gte: ?1, $lt: ?2 } }")
    List<Bills> findBillsForCurrentMonth(String userId, LocalDate startOfMonth, LocalDate startOfNextMonth);


    


}
