@Service
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "billing-topic", groupId = "email-notification-group")
    public void consume(String message) {
        LOGGER.info("Received message: {}", message);
        // Additional logic to process message can be added here.
    }
}
