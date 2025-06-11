package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;
import java.util.Optional;
import java.util.Map;

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
                      .findFirst()
                      .map(r -> r.getName())
                      .orElse("USER");

    // Generar el token JWT
    String token = jwtUtil.generateToken(user.getEmail(), role);

    // Retornar el token
    return ResponseEntity.ok().body(Map.of("token", token));
}
}