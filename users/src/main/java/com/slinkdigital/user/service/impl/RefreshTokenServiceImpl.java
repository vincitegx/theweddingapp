package com.slinkdigital.user.service.impl;

import com.slinkdigital.user.domain.RefreshToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.RefreshTokenRepository;
import com.slinkdigital.user.security.JwtProvider;
import com.slinkdigital.user.service.RefreshTokenService;
import static java.lang.Boolean.TRUE;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public String generateRefreshToken(Users user) {
        try{
            RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtProvider.getRefreshTokenExpirationInMillis()));
        refreshToken.setUser(user);
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
        }catch(RuntimeException ex){
            throw new UserException(ex.getMessage());
        }
        
    }
    
    @Override
    public Boolean deleteRefreshToken(String token) {
        try{
            refreshTokenRepository.deleteByToken(token);
        return TRUE;
        }catch(RuntimeException ex){
            throw new UserException(ex.getMessage());
        }        
    }
}
