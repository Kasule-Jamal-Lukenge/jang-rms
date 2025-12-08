package com.rms.jang_rms.auth;

import com.rms.jang_rms.auth.dtos.AuthRequest;
import com.rms.jang_rms.auth.dtos.AuthResponse;
import com.rms.jang_rms.auth.dtos.RefreshTokenRequest;
import com.rms.jang_rms.auth.dtos.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing token");
        }

        String token = authHeader.substring(7);

        blacklistedTokenRepository.save(
                BlacklistedToken.builder()
                        .token(token)
                        .build()
        );

        return ResponseEntity.ok("You Have Been Logged Out Successfully.");
    }

}

