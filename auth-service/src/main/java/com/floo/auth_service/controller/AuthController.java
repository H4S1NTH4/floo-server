package com.floo.auth_service.controller;
import com.floo.auth_service.dto.LoginRequest;
import com.floo.auth_service.dto.RegisterRequest;
import com.floo.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {


    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getProfile(request));
    }

    @GetMapping("/test")
    public String testAuthService() {
        return "Auth Service is running!";
    }
}


