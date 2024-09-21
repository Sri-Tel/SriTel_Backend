@Service
public class KafkaListenerService {

    @Autowired
    private SendMailService sendMailService;

    @KafkaListener(topics = "billing-topic", groupId = "email-notification-group")
    public void listenBillingTopic(String message) {
        // Assume message format: "User: user_email has paid amount"
        String[] parts = message.split(" ");
        String email = parts[1];
        String amount = parts[5];

        EmailDetailsDto emailDetails = new EmailDetailsDto();
        emailDetails.setRecipient(email);
        emailDetails.setEmailSubject("Payment Confirmation");
        emailDetails.setEmailBody("Dear User, your payment of " + amount + " has been received successfully.");

        sendMailService.sendSimpleMail(emailDetails);
    }
}
