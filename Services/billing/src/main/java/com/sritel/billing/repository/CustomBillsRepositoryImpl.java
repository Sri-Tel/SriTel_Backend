package com.sritel.billing.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBillsRepositoryImpl implements CustomBillsRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateBillStatusById(String id, String status) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("status", status);
        UpdateResult result = mongoTemplate.updateFirst(query, update, "bills");

        if (result.getMatchedCount() > 0) {
            System.out.println("Bill status updated successfully.");
        } else {
            System.out.println("No bill found with the given id.");
        }
    }
}
