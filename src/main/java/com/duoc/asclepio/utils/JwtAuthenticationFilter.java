package com.duoc.asclepio.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. RUTAS PÚBLICAS
        if (path.startsWith("/public") || path.equals("/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. VALIDACIÓN DE COMUNICACIÓN INTERNA (Service-to-Service)
        // Verificamos si la petición viene de nuestro otro microservicio
        String internalHeader = request.getHeader("X-Internal-Header");
        if ("MI_CLAVE_SECRETA_123".equals(internalHeader)) {
            filterChain.doFilter(request, response); // Deja pasar la petición
            return; // Termina la ejecución de este filtro para esta petición
        }

        // 3. VALIDACIÓN DE JWT (Para usuarios/Front-end)
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token faltante o inválido\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtTokenUtil.extractAllClaims(token);
            request.setAttribute("role", claims.get("role"));
            request.setAttribute("userId", claims.get("userId"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido: " + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}