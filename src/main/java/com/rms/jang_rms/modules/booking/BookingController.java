package com.rms.jang_rms.modules.booking;

import com.rms.jang_rms.dtos.CreateBookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingRequest req){
        return ResponseEntity.ok(bookingService.createBooking(req));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookings(){
        return ResponseEntity.ok(bookingService.getBookingsForTenant());
    }

    @GetMapping
    public ResponseEntity<?> allBookings(){
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.approveBooking(id));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.activateBooking(id));
    }
}
