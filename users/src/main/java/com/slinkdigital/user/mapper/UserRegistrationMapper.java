package com.slinkdigital.user.mapper;

import com.slinkdigital.user.constant.OAuth2Provider;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.RegisterRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class UserRegistrationMapper implements Function<RegisterRequest, Users> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Users apply(RegisterRequest registerRequest) {
        return Users.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .enabled(false)
                .nonLocked(false)
                .roles(new HashSet<>())
                .provider(OAuth2Provider.LOCAL)
                .build();
    }
}
