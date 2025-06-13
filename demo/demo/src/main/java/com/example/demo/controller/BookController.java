package com.example.demo.controller;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;
import com.example.demo.model.BookCopy;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.BookCopyService;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookCopyService bookCopyService;

    public BookController(BookService bookService, BookCopyService bookCopyService) {
        this.bookService = bookService;
        this.bookCopyService = bookCopyService;
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

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @PostMapping("/newcopy")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookCopy> createCopy(@RequestBody BookCopyDTO dto) {
        System.out.println(">>> AUTORIDADES: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok(bookCopyService.createCopy(dto));
    }

    @GetMapping("/find/{title}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Book> findBookByTitle(@PathVariable String title) {
        Book book = bookService.findBookByTitle(title);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
}