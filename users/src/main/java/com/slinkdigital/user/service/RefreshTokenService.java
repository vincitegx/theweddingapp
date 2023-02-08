package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.Users;


/**
 *
 * @author TEGA
 */
public interface RefreshTokenService {
//    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    
    String generateRefreshToken(Users user);
    
//    String validateRefreshToken(RefreshTokenRequest refreshTokenRequest); 
    
    Boolean deleteRefreshToken(String token);
}
