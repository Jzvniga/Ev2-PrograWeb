package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(unique = true)
    private String title;

    private String author;

    private String type; // novela, educación, etc.

    private Boolean active = true;
}