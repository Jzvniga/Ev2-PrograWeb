package com.example.demo.dto;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String email;
    private Long bookId;
}