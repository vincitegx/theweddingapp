package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.dto.UserRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TEGA
 */
public interface UserService {
    
    Map<String, String> registerUser(RegisterRequest registerRequest);
    
    Map<String, String> verifyEmail(String token);
    
    Map<String, String> requestNewVerificationToken(EmailRequest emailRequest);
    
    UserDto searchUser(String email);
    
    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    
    Boolean logout(RefreshTokenRequest refreshTokenRequest);

    JwtAuthResponse login(LoginRequest loginRequest);
    
    UserDto validateToken(String token);
    
    List<UserDto> getAllUsers();

    UserDto mapUserToDto(Users user);
    
    void addRoleCouple(List<Long> couple);
    
    Users getCurrentUser();

    Map<String, String> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Map<String, String> sendMessageToAdmin(UserRequest userRequest);
}
