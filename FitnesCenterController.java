import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fitness")

public class FitnesCenterController {
    private UserService userService;
    private SubscriptionService subscriptionService;
    private AuthenticationService authenticationService;
    private PaymentService paymentService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
       
        if (!userService.isEmailAvailable(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email sudah digunakan.");
        }

      
        if (!paymentService.isValidCreditCard(request.getCreditCard())) {
            return ResponseEntity.badRequest().body("Informasi kartu kredit tidak valid.");
        }

        userService.registerUser(request);

        return ResponseEntity.ok("Registrasi berhasil. Silakan konfirmasi email Anda.");
    }

    @PostMapping("/email-confirmation")
    public ResponseEntity<String> confirmEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        if (userService.confirmEmail(email, token)) {
            return ResponseEntity.ok("Email berhasil terkonfirmasi.");
        } else {
            return ResponseEntity.badRequest().body("Tautan konfirmasi tidak valid atau telah kadaluwarsa.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        if (authenticationService.authenticateUser(request.getEmail(), request.getPassword())) {
            String token = authenticationService.generateAuthToken(request.getEmail());
            return ResponseEntity.ok("Token sesi: " + token);
        } else {
            return ResponseEntity.badRequest().body("Login gagal. Cek kembali email dan password Anda.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email) {
      

        return ResponseEntity.ok("Instruksi reset password telah dikirim ke email Anda.");
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptions(@RequestParam("email") String email) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptions(email);
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
        if (subscriptionService.subscribe(request)) {
            return ResponseEntity.ok("Berlangganan berhasil.");
        } else {
            return ResponseEntity.badRequest().body("Gagal berlangganan.");
        }
    }

    @PostMapping("/cancel-subscription")
    public ResponseEntity<String> cancelSubscription(@RequestParam("email") String email, @RequestParam("subscriptionId") String subscriptionId) {
        if (subscriptionService.cancelSubscription(email, subscriptionId)) {
            return ResponseEntity.ok("Langganan berhasil dibatalkan.");
        } else {
            return ResponseEntity.badRequest().body("Gagal membatalkan langganan.");
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestParam("email") String email, @RequestParam("transactionId") String transactionId, @RequestParam("otp") String otp) {
        if (paymentService.verifyPayment(email, transactionId, otp)) {
            return ResponseEntity.ok("Pembayaran berhasil diverifikasi.");
        } else {
            return ResponseEntity.badRequest().body("Pembayaran tidak dapat diverifikasi.");
        }
    }

    @PostMapping("/update-info")
    public ResponseEntity<String> updateInfo(@RequestBody UpdateInfoRequest request) {
       

        return ResponseEntity.ok("Informasi pengguna berhasil diperbarui.");
    }
}
