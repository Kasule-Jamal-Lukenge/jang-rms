package com.rms.jang_rms.modules.payment.airtel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rms.jang_rms.modules.booking.Booking;
import com.rms.jang_rms.modules.payment.Payment;
import com.rms.jang_rms.modules.payment.PaymentRepository;
import com.rms.jang_rms.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AirtelMoneyService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final PaymentRepository paymentRepository;

    private String getAccessToken() {
//        String url = "https://api.airtel.com/oauth2/access_token";
        String url = "https://openapi.airtel.africa/auth/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
            "client_id": "YOUR_ID",
            "client_secret": "YOUR_SECRET",
            "grant_type": "client_credentials",
        }
        """;

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        try{
            JsonNode json = mapper.readTree(response.getBody());
            return json.get("access_token").asText();
        }catch (Exception e){
            throw new RuntimeException("Failed To Extract Airtel Access Token");
        }
    }

    public Payment initiatePayment(Booking booking, User payer){
        String accessToken = getAccessToken();

        String url = "https://openapi.airtel.africa/merchant/v1/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        double amount = booking.getProperty().getRentFee() * booking.getDurationMonths();

        String body = """
        {
        "reference": "%s",
        "subscriber": {"country:: "UG", "msisdn": "%s"},
        "transaction": {"amount": "%s", "country: "UG", "currency": "UGX"}
        """.formatted(booking.getId(), payer.getContact(), amount);

        Payment payment = Payment.builder()
                .booking(booking)
                .payer(payer)
                .amount(amount)
                .transactionId("AIRTEL-" + System.currentTimeMillis())
                .method(com.rms.jang_rms.enums.PaymentMethod.MOBILE_MONEY_AIRTEL)
                .status(com.rms.jang_rms.enums.PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }
}
