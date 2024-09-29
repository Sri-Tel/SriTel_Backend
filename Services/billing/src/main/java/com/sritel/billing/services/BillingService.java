package com.sritel.billing.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import com.sritel.billing.clients.PubSubClient;
import com.sritel.billing.dto.PublishRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.sritel.billing.clients.CustomerClient;
import com.sritel.billing.clients.MobileServiceClient;
import com.sritel.billing.dto.Customer;
import com.sritel.billing.dto.CustomerServicesResponse;
import com.sritel.billing.entity.Bills;
import com.sritel.billing.entity.ServiceStatus;
import com.sritel.billing.enums.UserGroup;
import com.sritel.billing.repository.BillsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillsRepository billingRepository;
    private final CustomerClient customerClient;
    private final MobileServiceClient mobileServiceClient;
    private final PubSubClient pubSubClient;
    @Autowired
    private MongoTemplate mongoTemplate;

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

            List<Bills> existingBills = billingRepository.findBillsForCurrentMonth(customer.getSritelNo(), startOfMonth, startOfNextMonth);

            // Skip bill generation if a bill for the current month already exists
            if (!existingBills.isEmpty()) {
                System.out.println("Bill already exists for user: " + customer.getSritelNo() + " for the current month.");
                continue;
            }

            //Get services for user
            List<CustomerServicesResponse> customerServices = mobileServiceClient.getServicesByUser(customer.getSritelNo());

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
                    .userId(customer.getSritelNo())
                    .amount(totalAmount)  // Calculate amount based on logic
                    .status("PENDING")
                    .billingDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(30))
                    .build();

            billingRepository.save(newBill);  // Save the bill
            System.out.println("Generated bill for user: " + customer.getSritelNo());
        }
    }

    public Bills createBilling(Bills billing) {
        return billingRepository.save(billing);
    }

    public List<Bills> getAllBillings() {
        return billingRepository.findAll();
    }

    public List<Bills> getBillingsByUserId(String userId) {
        return billingRepository.findAllByUserId(userId);
    }

    public void updateBillPayment(String billId) {
        // Logging the billId for debugging purposes
        System.out.println("Updating payment for Bill ID: " + billId);

        // Create a query to find the bill by ID
       Query query = new Query(Criteria.where("_id").is(new ObjectId(billId)));
       System.out.println("Mongo Query: " + query.toString());

        // Create an update object to set the status to 'PAID'
        Update update = new Update().set("status", "PAID");

        // Perform the update operation
        UpdateResult result = mongoTemplate.updateFirst(query, update, "bill");

        // Check if the update was successful
        if (result.getMatchedCount() > 0) {
            System.out.println("Bill status updated to 'PAID' successfully.");
        } else {
            System.out.println("No bill found with the given ID.");
        }

        Optional<Bills> bill = billingRepository.findById(billId);
        if (bill.isPresent()) {
            Customer customer = customerClient.getCustomerBySritelNo(bill.get().getUserId());
            if (customer != null) {
                PublishRequest request = new PublishRequest();
                request.setAmount(String.valueOf(bill.get().getAmount()));
                request.setEmail(String.valueOf(customer.getEmail()));

                pubSubClient.publish(request);
            }
        }

    }
}
