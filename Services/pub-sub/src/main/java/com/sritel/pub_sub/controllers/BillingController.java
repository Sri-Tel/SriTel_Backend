@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, String> billingDetails) {
        String message = "User: " + billingDetails.get("email") + " has paid " + billingDetails.get("amount");
        kafkaProducerService.sendBillingDetails(message);
        return ResponseEntity.ok("Message published successfully!");
    }
}
