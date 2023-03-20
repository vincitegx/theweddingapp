package com.slinkdigital.user.controller;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.ApiResponse;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.PasswordResetRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.service.AuthService;
import com.slinkdigital.user.service.EmailVerificationService;
import com.slinkdigital.user.service.PasswordService;
import com.slinkdigital.user.service.RefreshTokenService;
import com.slinkdigital.user.service.RoleService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import jakarta.validation.Valid;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/uu/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController2 {

    private final EmailVerificationService emailVerificationService;
    private final PasswordService passwordService;
    private final RoleService roleService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDto user = authService.registerUser(registerRequest);
        log.info(user.toString());
        String token = emailVerificationService.registerVerificationTokenToDb(user);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users").toUriString());
        return ResponseEntity.created(uri).body(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("token", token))
                        .status(CREATED)
                        .build()
        );
    }

    @PutMapping("vtoken")
    public ResponseEntity<ApiResponse> requestNewVerificationToken(@Valid @RequestBody EmailRequest emailRequest) {
        String token = emailVerificationService.requestNewVerificationToken(emailRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("token", token))
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("verify-email/{token}")
    public ResponseEntity<ApiResponse> verifyEmail(@PathVariable String token) {
        Users user = emailVerificationService.verifyEmail(token);
        UserDto userDto = roleService.setDefaultRole(user);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("user", userDto))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse jwtAuthResponse = authService.login(loginRequest);
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
                        .data(Map.of("JwtResponse", refreshTokenService.refreshToken(refreshTokenRequest)))
                        .message("Jwt Refreshed !!!")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("token/validate")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        UserDto user = authService.validateToken(token);
        log.info(user.toString());
        return ResponseEntity.ok(user);
    }

    @PostMapping("forget-password/generate-token")
    public ResponseEntity<ApiResponse> generateToken(@Valid @RequestBody EmailRequest emailRequest) {
        Map<String, String> resetTokenGenerationStatus = passwordService.generatePasswordResetToken(emailRequest);
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
        Map<String, String> resetAccountPasswordStatus = passwordService.resetAccountPassword(passwordResetRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isPasswordReseted", true))
                        .message(resetAccountPasswordStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was an error !!!")
    public ApiResponse handleError(HttpServletRequest req, Exception ex) {
        return ApiResponse.builder()
                .timeStamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
