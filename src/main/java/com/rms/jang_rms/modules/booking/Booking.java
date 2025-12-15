package com.rms.jang_rms.modules.booking;

import com.rms.jang_rms.enums.BookingStatus;
import com.rms.jang_rms.modules.property.Property;
import com.rms.jang_rms.modules.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private LocalDate checkInDate;

     private int durationMonths;

     private String notes;

     @Enumerated(EnumType.STRING)
     private BookingStatus status = BookingStatus.PENDING;

     @ManyToOne
     private User tenant;

     @ManyToOne
     private Property property;
}
