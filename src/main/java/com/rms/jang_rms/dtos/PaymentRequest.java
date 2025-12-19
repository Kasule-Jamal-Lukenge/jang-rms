package com.rms.jang_rms.dtos;

import com.rms.jang_rms.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private PaymentMethod paymentMethod;
}
