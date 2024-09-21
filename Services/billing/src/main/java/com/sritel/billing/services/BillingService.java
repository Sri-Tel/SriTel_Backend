package com.sritel.billing.services;

import com.sritel.billing.clients.CustomerClient;
import com.sritel.billing.clients.MobileServiceClient;
import com.sritel.billing.dto.BillResponse;
import com.sritel.billing.entity.Bills;
import com.sritel.billing.repository.BillsRepository;
import com.sritel.customer.entity.Customer;
import com.sritel.customer.enums.UserGroup;
import com.sritel.sritel_services.Response.CustomerServicesResponse;
import com.sritel.sritel_services.enums.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillsRepository billingRepository;
    private final CustomerClient customerClient;
    private final MobileServiceClient mobileServiceClient;

    // Automatically generate bills once a month (you can adjust the cron expression)
//    @Scheduled(cron = "0 0 2 1 * ?")  // Runs at 02:00 AM on the 1st of every month
    @Scheduled(cron = "0 * * * * *")  // Runs at 02:00 AM on the 1st of every month
    public void generateBillingForUserGroup1() {
        System.out.println("Generating bills for User Group 1...");
        generateBillsForUserGroup(UserGroup.GROUP1);
    }

    // Cron job for User Group 2 (e.g., on the 15th of every month at 02:00 AM)
//    @Scheduled(cron = "0 0 2 15 * ?")  // Runs at 02:00 AM on the 15th of every month
    @Scheduled(cron = "30 * * * * *")  // Runs at 02:00 AM on the 15th of every month
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

            // Check if a bill already exists for the current month
            YearMonth currentMonth = YearMonth.now();
            LocalDate startOfMonth = currentMonth.atDay(1);
            LocalDate startOfNextMonth = currentMonth.plusMonths(1).atDay(1);

            List<Bills> existingBills = billingRepository.findBillsForCurrentMonth(customer.getId(), startOfMonth, startOfNextMonth);

            // Skip bill generation if a bill for the current month already exists
            if (!existingBills.isEmpty()) {
                System.out.println("Bill already exists for user: " + customer.getId() + " for the current month.");
                continue;
            }

            //Get services for user
            List<CustomerServicesResponse> customerServices = mobileServiceClient.getServicesByUser(customer.getId());

//            if no services are registered to cutomer
            if (customerServices.size() <= 0) {
                continue;
            }


//           calcuate total amount
            BigDecimal totalAmount = customerServices.stream()
                    .filter(customerServiceResponse -> !ServiceStatus.INACTIVE.toString().equals(customerServiceResponse.getServiceStatus()))
                    .map(CustomerServicesResponse::getServiceAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


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
