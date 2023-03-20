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
import static java.lang.Boolean.TRUE;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final UserDtoMapper userDtoMapper;

    public String generateRefreshToken(Users user) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
            refreshToken.setUser(user);
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
    
    @CachePut(cacheNames = "userdto")
    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            RefreshToken refreshToken = validateRefreshToken(refreshTokenRequest);
            String token = jwtProvider.generateJwtToken(refreshToken);
            return JwtAuthResponse.builder()
                    .authToken(token)
                    .refreshToken(refreshTokenRequest.getRefreshToken())
                    .user(refreshTokenRequest.getUser())
                    .build();
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    public RefreshToken validateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                    .orElseThrow(() -> new UserException("Invalid refresh Token"));
            if (refreshToken.isExpired()) {
                refreshTokenRepository.delete(refreshToken);
                throw new UserException("Refresh token was expired. Please make a new signin request");
            }
            UserDto user = userDtoMapper.apply(refreshToken.getUser());
            if (!user.equals(refreshTokenRequest.getUser())) {
                throw new UserException("You will need to login again");
            }
            return refreshToken;
        } catch (UserException ex) {
            throw new UserException(ex.getMessage());
        }
    }
    
    public Boolean deleteRefreshToken(String token) {
        try {
            refreshTokenRepository.deleteByToken(token);
            return TRUE;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
}
