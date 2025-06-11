package com.example.demo.service;

import com.example.demo.model.Booking;
import com.example.demo.model.Reader;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ReaderRepository readerRepository;

    public BookingService(BookingRepository bookingRepository, ReaderRepository readerRepository) {
        this.bookingRepository = bookingRepository;
        this.readerRepository = readerRepository;
    }

    public List<Booking> getBookingsByEmail(String email) {
        return readerRepository.findByUserEmail(email)
                .map(bookingRepository::findByReader)
                .orElse(Collections.emptyList());
    }
}