package com.example.demo.repository;

import com.example.demo.model.BookCopy;
import com.example.demo.dto.BookCopyDTO;
import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBook(Book book);
    List<BookCopy> findByDisponibleTrue();
    @Query("SELECT new com.example.demo.dto.BookCopyDTO(c.id, b.id, " +
        "COALESCE(b.title, 'Sin título'), " +
        "COALESCE(b.author, 'Desconocido'), " +
        "COALESCE(b.type, 'General'), " +
        "c.disponible) " +
        "FROM BookCopy c JOIN c.book b " +
        "WHERE b.id = :bookId AND c.disponible = true")
    List<BookCopyDTO> findDTOByBookIdAndDisponibleTrue(@Param("bookId") Long bookId);
    List<BookCopy> findByBook_IdAndDisponibleTrue(@Param("bookId") Long bookId);


}
