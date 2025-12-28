package com.rms.jang_rms.modules.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDTO {
    private long totalProperties;
    private long availableProperties;
    private long bookedProperties;
    private long occupiedProperties;

    private long totalBookings;
    private long pendingBookings;
    private long approvedBookings;
    private long activeBookings;
    private long rejectedBookings;

    private double totalRevenue;

    private long totalTenants;
}
