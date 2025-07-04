package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.model.Book;
import com.example.demo.model.BookCopy;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    public BookCopyDTO createCopy(BookCopyDTO dto) {
        Book book = bookRepository.findById(dto.getBookId()).orElseThrow();
        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setDisponible(true);

        BookCopy saved = bookCopyRepository.save(copy);

        return new BookCopyDTO(
            saved.getId(),
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getType(),
            saved.getDisponible()
        );
    }

    public List<BookCopyDTO> getDisponiblesDTO(Long bookId) {
        return bookCopyRepository.findDTOByBookIdAndDisponibleTrue(bookId);
    }

}