package com.example.application;

import jakarta.annotation.PostConstruct;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextInitializer {

    @PostConstruct
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
