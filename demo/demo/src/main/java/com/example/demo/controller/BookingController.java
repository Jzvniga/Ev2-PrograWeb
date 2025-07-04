package com.example.demo.controller;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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
    public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable String email, Authentication auth) {
        String currentEmail = auth.getName();

        // Solo si es LECTOR, valida que sea su propio email
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("LECTOR")) &&
            auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            
            if (!email.equals(currentEmail)) {
                return ResponseEntity.status(403).build();
            }
        }

        List<Booking> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }
    
    @PostMapping("/return/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Booking> returnBooking(@PathVariable Long id) {
        System.out.println(">>> Entró al método returnBooking con id = " + id);
        return ResponseEntity.ok(bookingService.returnBooking(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('LECTOR')")
    public ResponseEntity<List<BookingDTO>> getMyBookings(Authentication auth) {
        String email = auth.getName();
        List<BookingDTO> bookings = bookingService.getBookingsDTOByEmail(email);
        return ResponseEntity.ok(bookings);
    }
}