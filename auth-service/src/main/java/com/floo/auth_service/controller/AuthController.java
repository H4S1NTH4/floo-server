package com.floo.auth_service.controller;

import com.floo.auth_service.dto.*;
import com.floo.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related endpoints such as register, login,
 * refresh token, logout, and user profile operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user (Admin, Driver, Restaurant Manager, or Customer).
     * @param request contains user info including role
     * @return AuthResponse with access and refresh tokens
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Log in with email and password.
     * Issues new access and refresh tokens.
     * @param request login credentials
     * @param response used to set secure HTTP-only cookie for refresh token
     * @return AuthResponse with token info and user details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    /**
     * Generate a new access token using a valid refresh token.
     * Refresh token should be in an HTTP-only cookie or Authorization header.
     * @param request used to read refresh token
     * @param response to send updated token (optional)
     * @return AuthResponse with new tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    /**
     * Invalidate refresh token and log the user out.
     * @param request contains refresh token (from cookie or header)
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Fetch the authenticated user's profile.
     * Extracts user ID from the JWT.
     * @param request contains Authorization header
     * @return full user profile object
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getProfile(request));
    }

    /**
     * Update the current authenticated user's profile.
     * Handles role-specific profile fields as well.
     * @param updateRequest user profile fields to update
     * @param request used to identify current user
     * @return updated profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest updateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(authService.updateProfile(updateRequest, request));
    }

    /**
     * Simple public endpoint to test if the auth service is up.
     * @return service status
     */
    @GetMapping("/test")
    public String testAuthService() {
        return "Auth Service is running!";
    }
}
