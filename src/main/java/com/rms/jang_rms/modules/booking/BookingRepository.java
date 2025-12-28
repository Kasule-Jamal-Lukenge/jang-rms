package com.rms.jang_rms.modules.booking;

import com.rms.jang_rms.enums.BookingStatus;
import com.rms.jang_rms.modules.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTenantId(Long tenantId);
    long countByStatus(BookingStatus bookingStatus);
    long countByPropertyIn(List<Property> properties);
    long countByPropertyInAndStatus(List<Property> properties, BookingStatus status);

}
