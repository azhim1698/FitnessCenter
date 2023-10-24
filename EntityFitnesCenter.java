@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String creditCardNumber;
    private String creditCardCVV;
    private Date creditCardExpiration;
}

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type; 
    private BigDecimal pricePerMeeting;
    private int maxSessions;
    private String description;
}

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private BigDecimal amount;
    private boolean isPaid;
    private String transactionId;
}
