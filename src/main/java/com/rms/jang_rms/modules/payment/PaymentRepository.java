package com.rms.jang_rms.modules.payment;

import com.rms.jang_rms.dtos.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
