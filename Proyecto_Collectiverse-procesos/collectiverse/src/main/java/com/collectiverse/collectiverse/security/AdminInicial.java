package com.collectiverse.collectiverse.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class AdminInicial implements CommandLineRunner {

    private final UserDetailsManager users;
    private final PasswordEncoder encoder;
    private final String adminPassword;

    public AdminInicial(UserDetailsManager users, PasswordEncoder encoder,
                        @Value("${ADMIN_PASSWORD:}") String adminPassword) {
        this.users = users;
        this.encoder = encoder;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (adminPassword.isBlank() || users.userExists("admin")) return;
        users.createUser(User.withUsername("admin")
                .password(encoder.encode(adminPassword))
                .roles("ADMIN", "USER")
                .build());
    }
}