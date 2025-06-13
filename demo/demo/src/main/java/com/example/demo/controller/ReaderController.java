package com.example.demo.controller;

import com.example.demo.service.ReaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    private final ReaderService readerService;
    

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping("/find/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getReaderDetails(@PathVariable String email) {
        Map<String, Object> details = readerService.getReaderDetailsByEmail(email);
        if (details == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(details);
    }

    @PostMapping("/state/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> toggleReaderState(@PathVariable String email) {
        boolean result = readerService.toggleReaderState(email);
        return ResponseEntity.ok(Map.of("updated", result));
    }

    @PutMapping("/toggle/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> toggleReaderStatus(@PathVariable String email) {
        System.out.println(">>> Toggling estado para lector con email: " + email);
        readerService.toggleReaderState(email);
        return ResponseEntity.ok().build();
    }
}