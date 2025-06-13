package com.example.demo.repository;

import com.example.demo.model.BookCopy;
import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBook(Book book);
    List<BookCopy> findByDisponibleTrue();
    List<BookCopy> findByBook_IdAndDisponibleTrue(Long bookId);
}