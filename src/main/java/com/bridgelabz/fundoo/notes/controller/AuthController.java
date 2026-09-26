package com.bridgelabz.fundoo.notes.controller;

import com.bridgelabz.fundoo.notes.dto.AuthResponseDTO;
import com.bridgelabz.fundoo.notes.dto.LoginRequestDTO;
import com.bridgelabz.fundoo.notes.dto.RegisterRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ResetPasswordRequestDTO;
import com.bridgelabz.fundoo.notes.redis.TokenCacheService;
import com.bridgelabz.fundoo.notes.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenCacheService tokenCacheService;

    public AuthController(AuthService authService, TokenCacheService tokenCacheService) {
        this.authService = authService;
        this.tokenCacheService = tokenCacheService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO) {

        authService.register(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO requestDTO) {

        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenCacheService.removeToken(token);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String resetToken = authService.forgotPassword(email);
        return ResponseEntity.ok(resetToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }
}
