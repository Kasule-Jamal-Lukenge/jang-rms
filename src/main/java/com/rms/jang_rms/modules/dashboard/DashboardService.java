package com.rms.jang_rms.modules.dashboard;

import com.rms.jang_rms.enums.BookingStatus;
import com.rms.jang_rms.enums.PropertyStatus;
import com.rms.jang_rms.modules.booking.BookingRepository;
import com.rms.jang_rms.modules.payment.PaymentRepository;
import com.rms.jang_rms.modules.property.PropertyRepository;
import com.rms.jang_rms.modules.user.User;
import com.rms.jang_rms.modules.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DashboardService {
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    private User getLoggedInUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }
    // -----------------------------------------ADMIN DASHBOARD-------------------------------------------------
    public DashboardSummaryDTO getAdminDashboard(){
        long totalProperties = propertyRepository.count();
        long availableProperties = propertyRepository.countByStatus(PropertyStatus.AVAILABLE);
        long bookedProperties = propertyRepository.countByStatus(PropertyStatus.BOOKED);
        long occupiedProperties = propertyRepository.countByStatus(PropertyStatus.OCCUPIED);

        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long approvedBookings = bookingRepository.countByStatus(BookingStatus.APPROVED);
        long rejectedBookings = bookingRepository.countByStatus(BookingStatus.REJECTED);
        long activeBookings = bookingRepository.countByStatus(BookingStatus.ACTIVE);

        double totalRevenue = paymentRepository.findAll().stream().mapToDouble(p -> p.getAmount()).sum();

        long totalTenants = userRepository.countByRoleName("ROLE_TENANT");

        return new DashboardSummaryDTO(
                totalProperties,
                availableProperties,
                bookedProperties,
                occupiedProperties,
                totalBookings,
                pendingBookings,
                approvedBookings,
                activeBookings,
                rejectedBookings,
                totalRevenue,
                totalTenants
        );
    }

    // -------------------- OWNER DASHBOARD --------------------
    public DashboardSummaryDTO getOwnerDashboard() {

        User owner = getLoggedInUser();

        long totalProperties = propertyRepository.countByOwner(owner);
        long availableProperties = propertyRepository.countByOwnerAndStatus(owner, PropertyStatus.AVAILABLE);
        long bookedProperties = propertyRepository.countByOwnerAndStatus(owner, PropertyStatus.BOOKED);
        long occupiedProperties = propertyRepository.countByOwnerAndStatus(owner, PropertyStatus.OCCUPIED);

        // Booking stats based on owner's properties
        var ownerProperties = propertyRepository.findByOwner(owner);

        long totalBookings = bookingRepository.countByPropertyIn(ownerProperties);
        long pendingBookings = bookingRepository.countByPropertyInAndStatus(ownerProperties, BookingStatus.PENDING);
        long approvedBookings = bookingRepository.countByPropertyInAndStatus(ownerProperties, BookingStatus.APPROVED);
        long activeBookings = bookingRepository.countByPropertyInAndStatus(ownerProperties, BookingStatus.ACTIVE);
        long rejectedBookings = bookingRepository.countByPropertyInAndStatus(ownerProperties, BookingStatus.REJECTED);

        // Revenue for owner's properties only
        double totalRevenue = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getBooking().getProperty().getOwner().getId().equals(owner.getId()))
                .mapToDouble(p -> p.getAmount())
                .sum();

        return new DashboardSummaryDTO(
                totalProperties,
                availableProperties,
                bookedProperties,
                occupiedProperties,
                totalBookings,
                pendingBookings,
                approvedBookings,
                activeBookings,
                rejectedBookings,
                totalRevenue,
                0   // Owner dashboard does not track total tenants
        );
    }
}
