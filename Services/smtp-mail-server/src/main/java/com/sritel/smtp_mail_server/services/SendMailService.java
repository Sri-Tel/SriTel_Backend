@Service
public class SendMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(EmailDetailsDto details) {
        try {
            LOGGER.info("Sending a simple mail without attachment file");
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getEmailBody());
            mailMessage.setSubject(details.getEmailSubject());

            javaMailSender.send(mailMessage);
        } catch (Exception exception) {
            LOGGER.error("Error while sending the mail", exception);
            throw new RuntimeException("Error while sending the mail: " + exception.getMessage());
        }
    }
}
