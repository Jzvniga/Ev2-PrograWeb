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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.example.demo.service.BookCopyService;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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
    public ResponseEntity<List<Book>> getAllBooks() {
            System.out.println(">>> USER: " + SecurityContextHolder.getContext().getAuthentication().getName());
            System.out.println(">>> AUTH: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok(bookService.getAllActiveBooks());
    }

    @GetMapping("/all/{type}")
    @PreAuthorize("hasAuthority('LECTOR') or hasAuthority('ADMIN')")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String type) {
        return ResponseEntity.ok(bookService.getBooksByType(type));
    }

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Book> createBookWithImage(
        @RequestParam("title") String title,
        @RequestParam("author") String author,
        @RequestParam("type") String type,
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        return ResponseEntity.ok(bookService.createBookWithImage(title, author, type, imageFile));
    }
    @PostMapping("/newcopy")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookCopyDTO> createCopy(@RequestBody BookCopyDTO dto) {
        return ResponseEntity.ok(bookCopyService.createCopy(dto));
    }

    @GetMapping("/disponibles/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BookCopyDTO>> getCopiasDisponibles(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookCopyService.getDisponiblesDTO(bookId));
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