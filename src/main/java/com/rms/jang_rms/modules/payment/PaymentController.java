package com.rms.jang_rms.modules.payment;

import com.rms.jang_rms.dtos.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> addPayment(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.processPayment(paymentRequest));
    }

    @PostMapping("/payments/mtn/callback")
    public ResponseEntity<String> handleMtnCallback(@RequestBody String payload){
        System.out.println("MTN Callback Received: " + payload);
        return ResponseEntity.ok("MTN Callback Received");
    }
}
