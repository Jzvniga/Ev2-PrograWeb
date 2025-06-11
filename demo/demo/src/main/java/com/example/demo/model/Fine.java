package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    private String descripcion;

    private Integer monto; // en pesos

    private Boolean estado = true; // true = activa, false = pagada/anulada
}