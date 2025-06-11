package com.example.demo.repository;

import com.example.demo.model.Booking;
import com.example.demo.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByReader(Reader reader);
}