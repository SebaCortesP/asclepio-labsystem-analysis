package com.duoc.asclepio.security;

import com.duoc.asclepio.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleGuardAspect {

    private final JwtTokenUtil jwtTokenUtil;
    private final HttpServletRequest request;

    @Around("@annotation(allowedRoles)")
    public Object checkRole(ProceedingJoinPoint joinPoint, AllowedRoles allowedRoles) throws Throwable {

        // 1 Extraer token del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado");
        }
        String token = authHeader.substring(7);

        // 2 Validar token
        if (!jwtTokenUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
        }

        // 3 Extraer rol del token
        String role = jwtTokenUtil.getRole(token);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol no encontrado en token");
        }

        // 4 Verificar si el rol está permitido
        for (String allowed : allowedRoles.value()) {
            if (allowed.equals(role)) {
                return joinPoint.proceed(); // pasa la ejecución al método
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para ejecutar esta acción");
    }
}
