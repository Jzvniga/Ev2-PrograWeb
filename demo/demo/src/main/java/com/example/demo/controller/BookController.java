package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('LECTOR') or hasAuthority('ADMIN')")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllActiveBooks());
    }

    @GetMapping("/all/{type}")
    @PreAuthorize("hasAuthority('LECTOR') or hasAuthority('ADMIN')")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String type) {
        return ResponseEntity.ok(bookService.getBooksByType(type));
    }
}