package com.example.demo.controller;

import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/find/{email}")
    @PreAuthorize("hasAuthority('LECTOR') or hasAuthority('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }
}