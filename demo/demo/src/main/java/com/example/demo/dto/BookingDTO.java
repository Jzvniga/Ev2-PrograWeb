package com.example.demo.dto;

import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    private Long copyId;
    private Long bookId;
    private String title;
    private String author;
    private String type;

    public BookingDTO(Long id, String fechaInicio, String fechaFin, String estado,
                      Long copyId, Long bookId, String title, String author, String type) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.copyId = copyId;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.type = type;
    }

    public BookingDTO() {}
}