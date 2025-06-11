package com.example.demo.repository;

import com.example.demo.model.Reader;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Optional<Reader> findByUserEmail(String email); // útil para /booking/find/{email}
    boolean existsByUser(User user); // por si quieres validar existencia
}