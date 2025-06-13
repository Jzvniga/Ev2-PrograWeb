package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final BookingRepository bookingRepository;
    private final FineRepository fineRepository;


    public ReaderService(ReaderRepository readerRepository, BookingRepository bookingRepository, FineRepository fineRepository) {
        this.readerRepository = readerRepository;
        this.bookingRepository = bookingRepository;
        this.fineRepository = fineRepository;
    }

    public Map<String, Object> getReaderDetailsByEmail(String email) {
        Map<String, Object> result = new HashMap<>();

        Reader reader = readerRepository.findByUserEmail(email).orElse(null);
        if (reader == null) return null;

        List<Booking> bookings = bookingRepository.findByReader(reader);
        List<Fine> fines = fineRepository.findByReader(reader);

        result.put("readerEmail", reader.getUser().getEmail());
        result.put("estado", reader.getEstado());
        result.put("prestamos", bookings);
        result.put("multas", fines);

        return result;
    }

    public boolean toggleReaderState(String email) {
        Optional<Reader> readerOpt = readerRepository.findByUserEmail(email);
        if (readerOpt.isEmpty()) {
            throw new RuntimeException("Lector no encontrado con email: " + email);
        }

        Reader reader = readerOpt.get();
        reader.setEstado(!reader.getEstado()); // Toggle
        readerRepository.save(reader);

        return reader.getEstado(); // Retorna el nuevo estado
    }
}