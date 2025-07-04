package com.example.demo.service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.model.Book;
import com.example.demo.model.BookCopy;
import com.example.demo.model.Booking;
import com.example.demo.model.Fine;
import com.example.demo.model.Reader;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.ReaderRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ReaderRepository readerRepository;
    private final BookCopyRepository bookCopyRepository;
    private final FineRepository fineRepository;

    public BookingService(BookingRepository bookingRepository,
                          ReaderRepository readerRepository,
                          BookCopyRepository bookCopyRepository,
                          FineRepository fineRepository) {
        this.bookingRepository = bookingRepository;
        this.readerRepository = readerRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.fineRepository = fineRepository;
    }

    // Nuevo método para usar en el controlador
    public List<BookingDTO> getBookingsDTOByEmail(String email) {
        return readerRepository.findByUserEmail(email)
                .map(reader -> bookingRepository.findByReader(reader).stream().map(b -> {
                    BookCopy copy = b.getBookCopy();
                    Book book = copy.getBook();

                    return new BookingDTO(
                        b.getId(),
                        b.getFechaInicio().toString(),
                        b.getFechaFin() != null ? b.getFechaFin().toString() : null,
                        b.getEstado() ? "Activo" : "Devuelto",
                        copy.getId(),
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getType()
                    );
                }).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    // Mismo método de uso interno (no se expone en API)
    public List<Booking> getBookingsByEmail(String email) {
        return readerRepository.findByUserEmail(email)
                .map(reader -> bookingRepository.findByReader(reader))
                .orElse(Collections.emptyList());
    }

    public Booking createBooking(BookingRequestDTO dto) {
        Reader reader = readerRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Reader no encontrado"));

        List<BookCopy> disponibles = bookCopyRepository.findByBook_IdAndDisponibleTrue(dto.getBookId());

        if (disponibles.isEmpty()) {
            throw new RuntimeException("No hay copias disponibles para el libro");
        }

        BookCopy selectedCopy = disponibles.get(0);
        Book book = selectedCopy.getBook();

        if (book == null) {
            throw new RuntimeException("La copia seleccionada no está asociada a ningún libro");
        }

        selectedCopy.setDisponible(false);
        bookCopyRepository.save(selectedCopy);

        Booking booking = new Booking();
        booking.setBookCopy(selectedCopy);
        booking.setReader(reader);
        booking.setFechaInicio(LocalDate.now());
        booking.setFechaFin(LocalDate.now().plusDays(5));
        booking.setEstado(true);

        return bookingRepository.save(booking);
    }

    public Booking returnBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking no encontrado"));

        if (!booking.getEstado()) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        booking.setEstado(false);
        bookingRepository.save(booking);

        BookCopy copy = booking.getBookCopy();
        copy.setDisponible(true);
        bookCopyRepository.save(copy);

        Book libro = copy.getBook();
        String tituloLibro = (libro != null) ? libro.getTitle() : "Libro desconocido";

        if (LocalDate.now().isAfter(booking.getFechaFin())) {
            Fine multa = new Fine();
            multa.setReader(booking.getReader());
            multa.setDescripcion("Multa por atraso en devolución del libro '" + tituloLibro + "'");
            multa.setMonto(3000);
            multa.setEstado(true);
            fineRepository.save(multa);
        }

        return booking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}