package com.example.demo.controller;

import com.example.demo.model.Fine;
import com.example.demo.service.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/fine")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

@GetMapping("/find/{email}")
@PreAuthorize("hasAuthority('LECTOR') or hasAuthority('ADMIN')")
public ResponseEntity<List<Fine>> getFinesByEmail(@PathVariable String email, Authentication auth) {
    String currentEmail = auth.getName();

    // Si es lector, solo puede ver sus propias multas
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("LECTOR"))) {
        if (!email.equals(currentEmail)) {
            return ResponseEntity.status(403).build(); // Acceso prohibido
        }
    }

    return ResponseEntity.ok(fineService.getFinesByEmail(email));
    }
}