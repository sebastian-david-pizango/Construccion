package com.collectiverse.collectiverse.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    public record RegisterRequest(String username, String password) {}

    private final UserDetailsManager users;
    private final PasswordEncoder encoder;

    public AuthController(UserDetailsManager users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (req.username() == null || req.username().isBlank() || req.username().length() > 50
                || req.password() == null || req.password().length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "username (máx 50) y password (mín 6) son obligatorios"));
        }
        if (users.userExists(req.username())) {
            return ResponseEntity.status(409).body(Map.of("error", "El usuario ya existe"));
        }
        users.createUser(User.withUsername(req.username())
                .password(encoder.encode(req.password()))
                .roles("USER")                       // siempre USER; ADMIN solo lo crea AdminInicial
                .build());
        return ResponseEntity.status(201).body(Map.of("username", req.username()));
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of("username", auth.getName(), "roles", auth.getAuthorities().toString());
    }

    @GetMapping("/admin/ping")
    public Map<String, String> adminPing() {
        return Map.of("msg", "hola admin");
    }
}