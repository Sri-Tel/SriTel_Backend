@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "billing-topic";

    public void sendBillingDetails(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
