package com.sritel.billing.services;

import com.sritel.billing.clients.CustomerClient;
import com.sritel.billing.dto.BillResponse;
import com.sritel.billing.entity.Bills;
import com.sritel.billing.repository.BillsRepository;
import com.sritel.customer.entity.Customer;
import com.sritel.customer.enums.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillsRepository billingRepository;
    private final CustomerClient customerClient;
//    private final MobileServiceClient mobileServiceClient;

    // Automatically generate bills once a month (you can adjust the cron expression)
    @Scheduled(cron = "0 0 2 1 * ?")  // Runs at 02:00 AM on the 1st of every month
    public void generateBillingForUserGroup1() {
        System.out.println("Generating bills for User Group 1...");
        generateBillsForUserGroup(UserGroup.GROUP1);
    }

    // Cron job for User Group 2 (e.g., on the 15th of every month at 02:00 AM)
    @Scheduled(cron = "0 0 2 15 * ?")  // Runs at 02:00 AM on the 15th of every month
    public void generateBillingForUserGroup2() {
        System.out.println("Generating bills for User Group 2...");
        generateBillsForUserGroup(UserGroup.GROUP2);
    }

    // Common method to generate bills for a specific user group
    public void generateBillsForUserGroup(UserGroup userGroup) {
        // Fetch all users for the given group (This should be your business logic)
        List<Customer> users = customerClient.getUsersByGroup(userGroup);

        // Iterate through each user and generate a bill
        for (Customer customer : users) {
            // Calculate total amount
//        BigDecimal totalAmount = services.stream()
//                .map(MobileService::getCost)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAmount = new BigDecimal(1522.56);

            Bills newBill = Bills.builder()
                    .userId(customer.getId())
                    .amount(totalAmount)  // Calculate amount based on logic
                    .status("PENDING")
                    .billingDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(30))
                    .build();

            billingRepository.save(newBill);  // Save the bill
            System.out.println("Generated bill for user: " + customer.getId());
        }
    }

    public Bills createBilling(Bills billing) {
        return billingRepository.save(billing);
    }

    public List<Bills> getAllBillings() {
        return billingRepository.findAll();
    }

    public List<Bills> getBillingsByUserId(String userId) {
        return billingRepository.findByUserId(userId);
    }

}
