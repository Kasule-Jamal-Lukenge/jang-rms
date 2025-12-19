package com.rms.jang_rms.modules.booking;

import com.rms.jang_rms.dtos.CreateBookingRequest;
import com.rms.jang_rms.enums.BookingStatus;
import com.rms.jang_rms.modules.property.Property;
import com.rms.jang_rms.modules.property.PropertyRepository;
import com.rms.jang_rms.modules.user.User;
import com.rms.jang_rms.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    private User getLoggedInUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public Booking createBooking(CreateBookingRequest createBookingRequest){
        User tenant = getLoggedInUser();
        Property property = propertyRepository.findById(createBookingRequest.getPropertyId()).orElseThrow(() -> new RuntimeException("Property Not Found"));
        Booking booking = Booking.builder()
                .checkInDate(createBookingRequest.getCheckInDate())
                .durationMonths(createBookingRequest.getDurationMonths())
                .notes(createBookingRequest.getNotes())
                .tenant(tenant)
                .property(property)
                .status(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForTenant(){
        User tenant = getLoggedInUser();
        return bookingRepository.findByTenantId(tenant.getId());
    }

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking approveBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking Not Found"));
        User currentUser = getLoggedInUser();

        // OWNER or ADMIN rule
        boolean isAdmin = currentUser.getRole().getName().equals("ROLE_ADMIN");
        boolean isOwner = booking.getProperty().getOwner().getId().equals(currentUser.getId());

        if(!isAdmin && !isOwner){
            throw new RuntimeException("You Are Not Allowed To Approve This Booking");
        }

        booking.setStatus(BookingStatus.APPROVED);
        booking.getProperty().setStatus(com.rms.jang_rms.enums.PropertyStatus.BOOKED);

        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking Not Found"));
        User currentUser = getLoggedInUser();

        boolean isAdmin = currentUser.getRole().getName().equals("ROLE_ADMIN");
        boolean isOwner = booking.getProperty().getOwner().getId().equals(currentUser.getId());

        if(!isAdmin && !isOwner){
            throw new RuntimeException("You Are Not Allowed To Reject This Booking");
        }

        booking.setStatus(BookingStatus.REJECTED);

        return bookingRepository.save(booking);
    }

    public Booking activateBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking Not Found"));

        booking.setStatus(BookingStatus.ACTIVE);
        booking.getProperty().setStatus(com.rms.jang_rms.enums.PropertyStatus.OCCUPIED);

        return bookingRepository.save(booking);
    }
}
