package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta para firmar el token (no exponer en producción)
    private static final String SECRET_KEY = "mi_clave_ultra_secreta";

    // Duración del token: 1 hora
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    // Generar el token con email y rol
    public String generateToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // Validar token y extraer el email
    public String validateTokenAndRetrieveSubject(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
        return jwt.getSubject(); // Devuelve el email
    }

    // Obtener el rol desde el token
    public String getRoleFromToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
        return jwt.getClaim("role").asString();
    }
}