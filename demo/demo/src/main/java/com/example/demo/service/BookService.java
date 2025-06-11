package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllActiveBooks() {
        return bookRepository.findByActiveTrue();
    }

    public List<Book> getBooksByType(String type) {
        return bookRepository.findByTypeIgnoreCase(type);
    }
}