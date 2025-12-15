package com.rms.jang_rms.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    private LocalDate checkInDate;
    private int durationMonths;
    private String notes;
    private Long propertyId;
}
