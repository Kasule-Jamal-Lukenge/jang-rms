package com.rms.jang_rms.modules.payment;

import com.rms.jang_rms.dtos.PaymentRequest;
import com.rms.jang_rms.enums.PaymentStatus;
import com.rms.jang_rms.enums.PropertyStatus;
import com.rms.jang_rms.modules.booking.Booking;
import com.rms.jang_rms.modules.booking.BookingRepository;
import com.rms.jang_rms.modules.user.User;
import com.rms.jang_rms.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public Payment processPayment(PaymentRequest paymentRequest) {
        User payer = getLoggedInUser();

        Booking booking = bookingRepository.findById(paymentRequest.getBookingId()).orElseThrow(() -> new RuntimeException("Booking Not Found"));

        if(!booking.getTenant().getId().equals(payer.getId())) {
            throw new RuntimeException("Booking is not authorized");
        }

        double amount = booking.getProperty().getRentFee() * booking.getDurationMonths();

        Payment payment = Payment.builder()
                .amount(amount)
                .method(paymentRequest.getPaymentMethod())
                .payer(payer)
                .booking(booking)
                .transactionId("TRX-" + System.currentTimeMillis())
                .status(PaymentStatus.SUCCESS)
                .build();

        paymentRepository.save(payment);

        // Auto-activate booking after payment
        booking.setStatus(com.rms.jang_rms.enums.BookingStatus.ACTIVE);
        booking.getProperty().setStatus(PropertyStatus.OCCUPIED);
        bookingRepository.save(booking);

        return payment;
    }
}
