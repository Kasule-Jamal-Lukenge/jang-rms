package com.rms.jang_rms.modules.payment;

import com.rms.jang_rms.enums.PaymentMethod;
import com.rms.jang_rms.enums.PaymentStatus;
import com.rms.jang_rms.modules.booking.Booking;
import com.rms.jang_rms.modules.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String transactionId;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private User payer;
}
