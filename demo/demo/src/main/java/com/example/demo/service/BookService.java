package com.example.demo.service;

import com.example.demo.dto.BookDTO;
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
    public Book createBook(BookDTO dto) {
    Book book = new Book();
    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());
    book.setType(dto.getType());
    book.setActive(true);
    return bookRepository.save(book);
    }

    public Book findBookByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).orElse(null);
    }
}