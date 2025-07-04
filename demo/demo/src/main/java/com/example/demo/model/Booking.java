package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_copy_id")
    @JsonIgnoreProperties({"book", "hibernateLazyInitializer", "handler"})
    private BookCopy bookCopy;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Boolean estado = true; // true = activo, false = devuelto
}