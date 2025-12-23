package com.rms.jang_rms.modules.payment.mtn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rms.jang_rms.config.MtnMomoConfig;
import com.rms.jang_rms.modules.booking.Booking;
import com.rms.jang_rms.modules.payment.Payment;
import com.rms.jang_rms.modules.payment.PaymentRepository;
import com.rms.jang_rms.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MtnMomoService {

    private final MtnMomoConfig mtnMomoConfig;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // STEP 1: Generating Access Token
    public String getAccessToken() {
        String url = mtnMomoConfig.getBaseUrl().replace("collection/v1_0", "collection/token/");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", mtnMomoConfig.getSubscriptionKey());
        headers.setBasicAuth(mtnMomoConfig.getApiUser(), mtnMomoConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        try{
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        }catch(Exception e){
            throw new RuntimeException("Failed To Parse MTN Token Response");
        }
    }

    //  STEP 2: Request To Pay (Initiate Momo Payment)
    public Payment initiatePayment(Booking booking, User payer){
        String transactionId = UUID.randomUUID().toString();
        String accessToken = getAccessToken();

        String url = mtnMomoConfig.getBaseUrl() + "/requesttopay";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-Reference-Id", transactionId);
        headers.set("Ocp-Apim-Subscription-Key", mtnMomoConfig.getSubscriptionKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        double amount = booking.getProperty().getRentFee() * booking.getDurationMonths();

        String body = """
        {
          "amount": "%s",
          "currency": "UGX,
          "externalId": "%s",
          "payer": {
            "partyIdType": "MSISDN",
            "partyId": "%s",
          },
          "payerMessage": "Rental Payment",
          "payeeNote": "Rental Service",
          "callbackUrl": "%s",
        }
        """.formatted(amount, booking.getId(), payer.getContact(), mtnMomoConfig.getCallbackUrl());

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

    // Saving The Pending Payment In The Database
        Payment payment = Payment.builder()
                .booking(booking)
                .payer(payer)
                .transactionId(transactionId)
                .amount(amount)
                .method(com.rms.jang_rms.enums.PaymentMethod.MOBILE_MONEY_MTN)
                .status(com.rms.jang_rms.enums.PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    // STEP 3: Check Payment Status
    public String checkTransactionStatus(String transactionId){
        String url = mtnMomoConfig.getBaseUrl() + "/requesttopay" + transactionId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());
        headers.set("Ocp-Apim-Subscription-Key", mtnMomoConfig.getSubscriptionKey());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
