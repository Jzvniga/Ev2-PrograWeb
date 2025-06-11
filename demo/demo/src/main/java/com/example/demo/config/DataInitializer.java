package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import java.time.LocalDate;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;
    private final BookCopyRepository bookCopyRepository;
    private final BookingRepository bookingRepository;
    private final FineRepository fineRepository;
    

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("LECTOR");
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName).ifPresentOrElse(
            r -> {}, // Ya existe
            () -> {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Rol creado: " + roleName);
            }
        );
    }
    @PostConstruct
    public void initData() {
        initRoles();
        initBooks();
        initReaders();
        initBookings();
        initFines();
    }

    private void initBooks() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book(null, "Cien años de soledad", "Gabriel García Márquez", "Novela", true));
            bookRepository.save(new Book(null, "La Odisea", "Homero", "Aventura", true));
            bookRepository.save(new Book(null, "El Quijote", "Miguel de Cervantes", "Novela", true));
            System.out.println("Libros de prueba insertados.");
        }

    }
        private void initReaders() {
        createReaderIfNotExists("usuario@ejemplo.com");
        createReaderIfNotExists("usuario1@ejemplo.com");
    }

    private void createReaderIfNotExists(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean exists = readerRepository.existsByUser(user);
            if (!exists) {
                Reader reader = new Reader();
                reader.setUser(user);
                reader.setEstado(true);
                readerRepository.save(reader);
                System.out.println("Reader creado para usuario: " + email);
            } else {
                System.out.println("Ya existe Reader para: " + email);
            }
        } else {
            System.out.println("Usuario no encontrado: " + email);
        }
    }
        private void initBookings() {
    Optional<User> userOpt = userRepository.findByEmail("usuario@ejemplo.com");
    Optional<Book> bookOpt = bookRepository.findAll().stream().findFirst(); // Usa cualquier libro existente

    if (userOpt.isPresent() && bookOpt.isPresent()) {
        User user = userOpt.get();
        Reader reader = readerRepository.findByUserEmail(user.getEmail()).orElseThrow();
        Book book = bookOpt.get();

        // Crear copia
        BookCopy copy = new BookCopy(null, book, true);
        bookCopyRepository.save(copy);

        // Crear booking
        Booking booking = new Booking();
        booking.setReader(reader);
        booking.setBookCopy(copy);
        booking.setFechaInicio(LocalDate.now());
        booking.setFechaFin(LocalDate.now().plusDays(5));
        booking.setEstado(true);
        bookingRepository.save(booking);

        System.out.println("Booking de prueba creado para " + user.getEmail());
        }  
    }
    private void initFines() {
        readerRepository.findByUserEmail("usuario@ejemplo.com").ifPresent(reader -> {
            Fine fine = new Fine();
            fine.setReader(reader);
            fine.setDescripcion("Multa por retraso en devolución de 'El Quijote'");
            fine.setMonto(3000);
            fine.setEstado(true);
            fineRepository.save(fine);
            System.out.println("Multa de prueba creada.");
        });
    }
}