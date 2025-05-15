package com.floo.auth_service.service;

import com.floo.auth_service.config.AuthProperties;
import com.floo.auth_service.dto.*;
import com.floo.auth_service.exception.EmailAlreadyExistsException;
import com.floo.auth_service.model.*;
import com.floo.auth_service.repository.RefreshTokenRepository;
import com.floo.auth_service.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthProperties authProperties;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        Profile profile = request.getProfile() != null ?
                request.getProfile() :
                Profile.builder().fullName(request.getUsername()).build();

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .profile(profile)
                .driverProfile(request.getDriverProfile())
                .restaurantProfile(request.getRestaurantProfile())
                .build();

        userRepository.save(user);

        return generateAuthResponse(user, true);
    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return generateAuthResponse(user, true);
    }

    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is missing");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateAuthResponse(user, false); // Don't regenerate refresh token
    }

    public void logout(HttpServletRequest request) {
        String refreshToken = jwtService.extractRefreshToken(request);
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(refreshTokenRepository::delete);
        }
    }

    public User getProfile(HttpServletRequest request) {
        String token = jwtService.resolveToken(request);
        String email = jwtService.getEmailFromToken(token);
        return userRepository.findByEmail(email).orElseThrow();

        // TODO: Use a custom response without the password in it

    }

    public User updateProfile(UpdateProfileRequest updateRequest, HttpServletRequest request) {
        User user = getProfile(request);

        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getProfile() != null) {
            user.setProfile(updateRequest.getProfile());
        }

        if (updateRequest.getDriverProfile() != null) {
            user.setDriverProfile(updateRequest.getDriverProfile());
        }

        if (updateRequest.getRestaurantProfile() != null) {
            user.setRestaurantProfile(updateRequest.getRestaurantProfile());
        }

        return userRepository.save(user);
    }

    private AuthResponse generateAuthResponse(User user, boolean generateNewRefreshToken) {
        String accessToken = jwtService.generateToken(user);

        String refreshToken = null;
        if (generateNewRefreshToken) {
            refreshToken = UUID.randomUUID().toString();

            // Save refresh token in DB
            RefreshToken tokenEntity = RefreshToken.builder()
                    .userId(user.getId())
                    .token(refreshToken)
                    .expiryDate(new Date(System.currentTimeMillis() + authProperties.getRefreshTokenExpiryMs()))
                    .build();

            refreshTokenRepository.save(tokenEntity);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .profile(user.getProfile())
                        .build())
                .build();
    }
}
