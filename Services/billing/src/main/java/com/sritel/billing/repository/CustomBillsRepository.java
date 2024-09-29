package com.sritel.billing.repository;

public interface CustomBillsRepository {
    void updateBillStatusById(String id, String status);
}
