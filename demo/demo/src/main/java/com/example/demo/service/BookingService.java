package com.example.demo.service;

import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.model.BookCopy;
import com.example.demo.model.Booking;
import com.example.demo.model.Fine;
import com.example.demo.model.Reader;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.FineRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ReaderRepository readerRepository;
    private final BookCopyRepository bookCopyRepository;
    private final FineRepository fineRepository;


    public BookingService(BookingRepository bookingRepository, ReaderRepository readerRepository, 
                          BookCopyRepository bookCopyRepository, FineRepository fineRepository) {
        this.bookingRepository = bookingRepository;
        this.readerRepository = readerRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.fineRepository = fineRepository;
    }

    public List<Booking> getBookingsByEmail(String email) {
        return readerRepository.findByUserEmail(email)
                .map(bookingRepository::findByReader)
                .orElse(Collections.emptyList());
    }

    public Booking createBooking(BookingRequestDTO dto) {
        Reader reader = readerRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Reader no encontrado"));

        List<BookCopy> disponibles = bookCopyRepository.findByBook_IdAndDisponibleTrue(dto.getBookId());

        if (disponibles.isEmpty()) {
            throw new RuntimeException("No hay copias disponibles para el libro");
        }

        BookCopy selectedCopy = disponibles.get(0); // se puede aplicar lógica de prioridad si se desea
        selectedCopy.setDisponible(false); // marcar como prestada
        bookCopyRepository.save(selectedCopy);

        Booking booking = new Booking();
        booking.setBookCopy(selectedCopy);
        booking.setReader(reader);
        booking.setFechaInicio(LocalDate.now());
        booking.setFechaFin(LocalDate.now().plusDays(5));
        booking.setEstado(true); // activa

        return bookingRepository.save(booking); // 🔥 ESTE ES EL RETURN FALTANTE
    }


        public Booking returnBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));

        if (!booking.getEstado()) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        // Marcar el préstamo como devuelto
        booking.setEstado(false);
        bookingRepository.save(booking);

        // Marcar la copia como disponible
        BookCopy copy = booking.getBookCopy();
        copy.setDisponible(true);
        bookCopyRepository.save(copy);

        // Verificar si se pasó la fecha
        if (LocalDate.now().isAfter(booking.getFechaFin())) {
            Fine multa = new Fine();
            multa.setReader(booking.getReader());
            multa.setDescripcion("Multa por atraso en devolución del libro '" + copy.getBook().getTitle() + "'");
            multa.setMonto(3000); // Monto fijo, puedes mejorarlo después
            multa.setEstado(true);
            fineRepository.save(multa);
        }

            return booking;
        }

        public List<Booking> getAllBookings() {
            return bookingRepository.findAll();
        }
}