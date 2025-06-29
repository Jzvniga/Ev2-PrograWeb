package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    
    
    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }
    @PostMapping("/register-lector")
    public ResponseEntity<?> registerLector(@RequestBody UserDTO userDTO) {
    try {
        userService.registerLector(userDTO);
        return ResponseEntity.ok("Lector registrado exitosamente");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // Obtener el primer rol del usuario
            String role = user.getRoles().stream()
                .map(r -> r.getName())
                .filter(r -> r.equals("ADMIN"))
                .findFirst()
                .orElse("LECTOR"); 

        // Generar el token JWT
        String token = jwtUtil.generateToken(user.getEmail(), role);

        // Retornar el token
        return ResponseEntity.ok().body(Map.of(
            "token", token,
            "role", role
     
));
}
}