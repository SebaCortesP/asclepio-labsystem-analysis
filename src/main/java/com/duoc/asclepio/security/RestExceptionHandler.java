package com.duoc.asclepio.security;

import com.duoc.asclepio.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401).body(new ApiResponse<>(false, ex.getReason(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(new ApiResponse<>(false, ex.getReason(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception ex) {
        // opcional: registrar ex.getMessage()
        return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error interno", null));
    }
}
