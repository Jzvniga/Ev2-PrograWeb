package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllActiveBooks() {
        return bookRepository.findByActiveTrue();
    }

    public List<Book> getBooksByType(String type) {
        return bookRepository.findByTypeIgnoreCase(type);
    }
    public Book createBookWithImage(String title, String author, String type, MultipartFile imageFile) {
        String base64Image = "";
        try {
            base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen", e);
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setType(type);
        book.setActive(true);
        book.setImage(base64Image);

        return bookRepository.save(book);
    }
    @Transactional
    public Book findBookByTitle(String title) {
        return bookRepository.findByTitleIgnoreCaseAndActiveTrue(title).orElse(null);
    }

}