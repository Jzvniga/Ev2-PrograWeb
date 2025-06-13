package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.model.Book;
import com.example.demo.model.BookCopy;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    public BookCopy createCopy(BookCopyDTO dto) {
        Book book = bookRepository.findById(dto.getBookId()).orElseThrow();
        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setDisponible(true);
        return bookCopyRepository.save(copy);
    }
}