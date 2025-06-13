package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTypeIgnoreCase(String type);
    List<Book> findByActiveTrue(); // solo los activos
    Optional<Book> findByTitleContainingIgnoreCase(String title);
}
