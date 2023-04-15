package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.RefreshToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import com.slinkdigital.user.repository.RefreshTokenRepository;
import com.slinkdigital.user.security.JwtProvider;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final UserDtoMapper userDtoMapper;
    private final Clock clock;

    public String generateRefreshToken(Users user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        refreshToken.setUser(user);
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @CachePut(cacheNames = "userdto")
    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = validateRefreshToken(refreshTokenRequest);
        String token = jwtProvider.generateJwtToken(refreshToken);
        return JwtAuthResponse.builder()
                .authToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .user(refreshTokenRequest.getUser())
                .build();
    }

    public RefreshToken validateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new UserException("Invalid refresh Token"));
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now(clock))) {
            refreshTokenRepository.delete(refreshToken);
            throw new UserException("Refresh token was expired. Please make a new signin request");
        }
        UserDto user = userDtoMapper.apply(refreshToken.getUser());
        if (!user.equals(refreshTokenRequest.getUser())) {
            throw new UserException("You will need to login again");
        }
        return refreshToken;
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
