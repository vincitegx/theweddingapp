package com.slinkdigital.user.controller;

import com.slinkdigital.user.dto.ApiResponse;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.PasswordResetRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.service.PasswordResetService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.Valid;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.slinkdigital.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/uu/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController2 {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        Map<String, String> registrationStatus = userService.registerUser(registerRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users").toUriString());
        return ResponseEntity.created(uri).body(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isRegistered", true))
                        .message(registrationStatus.get("success"))
                        .status(CREATED)
                        .build()
        );
    }

    @PostMapping("request-new-verification-token")
    public ResponseEntity<ApiResponse> requestNewVerificationToken(@Valid @RequestBody EmailRequest emailRequest) {
        Map<String, String> requestStatus = userService.requestNewVerificationToken(emailRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isNewVerificationTokenGenerated", true))
                        .message(requestStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("verify-email/{token}")
    public ResponseEntity<ApiResponse> verifyEmail(@PathVariable String token) {
        Map<String, String> verificationStatus = userService.verifyEmail(token);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isEmailVerified", true))
                        .message(verificationStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse jwtAuthResponse = userService.login(loginRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("JwtResponse", jwtAuthResponse))
                        .message("Login Successful !!!")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("refresh/token")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("JwtResponse", userService.refreshToken(refreshTokenRequest)))
                        .message("Jwt Refreshed !!!")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("token/validate")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return ResponseEntity.ok(userService.validateToken(token));
    }

    @PostMapping("forget-password/generate-token")
    public ResponseEntity<ApiResponse> generateToken(@Valid @RequestBody EmailRequest emailRequest) {
        Map<String, String> resetTokenGenerationStatus = passwordResetService.generatePasswordResetToken(emailRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isResetTokenCreated", true))
                        .message(resetTokenGenerationStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("forget-password/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        Map<String, String> resetAccountPasswordStatus = passwordResetService.resetAccountPassword(passwordResetRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isPasswordReseted", true))
                        .message(resetAccountPasswordStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }
}
