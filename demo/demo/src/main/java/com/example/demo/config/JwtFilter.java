package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.security.JwtUtil;
import java.io.IOException;
import java.util.List;
import org.springframework.lang.NonNull;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String email = jwtUtil.validateTokenAndRetrieveSubject(token);
                String role = jwtUtil.getRoleFromToken(token);

                //  Aquí se agrega el rol como autoridad
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                System.out.println(">>> ROLE INJECTADO: " + role);
                System.out.println(">>> Authorities: " + authentication.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("Token válido: " + token);
                System.out.println("Email: " + email);
                System.out.println("Rol: " + role);
            } catch (Exception e) {
                System.out.println("ERROR JWT: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}