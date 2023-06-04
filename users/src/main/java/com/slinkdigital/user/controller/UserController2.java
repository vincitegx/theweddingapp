package com.slinkdigital.user.controller;

import com.slinkdigital.user.domain.Users;
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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDto user = authService.registerUser(registerRequest);
        emailVerificationService.registerVerificationTokenToDb(user);
    }

    @PutMapping("vtoken")
    @ResponseStatus(HttpStatus.OK)
    public void requestNewVerificationToken(@Valid @RequestBody EmailRequest emailRequest) {
        emailVerificationService.requestNewVerificationToken(emailRequest);
    }

    @GetMapping("verify-email/{token}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto verifyEmail(@PathVariable String token) {
        Users user = emailVerificationService.verifyEmail(token);
        return roleService.setDefaultRole(user);
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthResponse userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

//    @PostMapping("/oauth2/callback")
//    public void handleOAuth2Authentication(@RequestBody OAuth2Authentication auth) {
//        Authentication userAuthentication = auth.getUserAuthentication();
//        String username = userAuthentication.getName();
//    }
    
    @PostMapping("refresh/token")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("token/validate")
    @ResponseStatus(HttpStatus.OK)
    public UserDto validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return authService.validateToken(token);
    }

    @PostMapping("forget-password/generate-token")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateToken(@Valid @RequestBody EmailRequest emailRequest) {
        passwordService.generatePasswordResetToken(emailRequest);
    }

    @PostMapping("forget-password/reset-password")
    public void resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordService.resetAccountPassword(passwordResetRequest);
    }
}
