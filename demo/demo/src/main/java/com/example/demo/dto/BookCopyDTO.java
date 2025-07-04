package com.example.demo.dto;

import lombok.Data;

@Data
public class BookCopyDTO {
    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private String type;
    private Boolean disponible;
    
    public BookCopyDTO(Long id, Long bookId, String title, String author, String type, Boolean disponible) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.type = type;
        this.disponible = disponible;
    }

    // Constructor vacío requerido por JPA/Spring
    public BookCopyDTO() {}

    // Getters y setters (puedes usar @Data de Lombok si quieres)
}