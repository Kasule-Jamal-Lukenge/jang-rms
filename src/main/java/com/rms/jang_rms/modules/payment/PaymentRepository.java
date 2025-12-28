package com.rms.jang_rms.modules.payment;

import com.rms.jang_rms.dtos.PaymentRequest;
import com.rms.jang_rms.modules.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
