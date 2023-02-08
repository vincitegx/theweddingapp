package com.slinkdigital.user.service.impl;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import com.slinkdigital.user.repository.RoleRepository;
import com.slinkdigital.user.repository.UserRepository;
import com.slinkdigital.user.security.JwtProvider;
import com.slinkdigital.user.service.EmailVerificationService;
import com.slinkdigital.user.service.RefreshTokenService;
import com.slinkdigital.user.validator.EmailValidator;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author TEGA
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    
//    private UserServiceImpl underTest;
//    @Mock
//    private UserRepository userRepository;
//    private EmailVerificationTokenRepository emailVerificationTokenRepository;
//    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;
//    private EmailVerificationService emailVerificationService;
//    private JwtProvider jwtProvider;
//    private AuthenticationManager authenticationManager;
//    private RefreshTokenService refreshTokenService;
//    private EmailValidator emailValidator;
//    
//    public UserServiceImplTest() {
//    }
//    
//    @BeforeAll
//    public static void setUpClass() {
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//    }
//    
//    @BeforeEach
//    public void setUp() {
//        underTest = new UserServiceImpl(userRepository, emailVerificationTokenRepository, roleRepository, passwordEncoder, emailVerificationService, jwtProvider, authenticationManager, refreshTokenService, emailValidator);
//    }
//
//    @Test
//    @Disabled
//    public void testRegisterUser() {
//        System.out.println("registerUser");
//        RegisterRequest registerRequest = null;
//        UserServiceImpl instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.registerUser(registerRequest);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testVerifyEmail() {
//        System.out.println("verifyEmail");
//        String token = "";
//        UserServiceImpl instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.verifyEmail(token);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testRequestNewVerificationToken() {
//        System.out.println("requestNewVerificationToken");
//        EmailRequest emailRequest = null;
//        UserServiceImpl instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.requestNewVerificationToken(emailRequest);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testLogin() {
//        System.out.println("login");
//        LoginRequest loginRequest = null;
//        UserServiceImpl instance = null;
//        JwtAuthResponse expResult = null;
//        JwtAuthResponse result = instance.login(loginRequest);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testRefreshToken() {
//        System.out.println("refreshToken");
//        RefreshTokenRequest refreshTokenRequest = null;
//        UserServiceImpl instance = null;
//        JwtAuthResponse expResult = null;
//        JwtAuthResponse result = instance.refreshToken(refreshTokenRequest);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testIsLoggedIn() {
//        System.out.println("isLoggedIn");
//        UserServiceImpl instance = null;
//        boolean expResult = false;
//        boolean result = instance.isLoggedIn();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    @Disabled
//    public void testLogout() {
//        System.out.println("logout");
//        RefreshTokenRequest refreshTokenRequest = null;
//        UserServiceImpl instance = null;
//        Boolean expResult = null;
//        Boolean result = instance.logout(refreshTokenRequest);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetAllUsers() {
//        underTest.getAllUsers();
//        verify(userRepository).findAll();
//    }
//    
//    private UserDto mapUserToDto(Users user){
//        UserDto userDto =  UserDto.builder().email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build();
//        return userDto;
//    }
//
//    @Test
//    @Disabled
//    public void testSearchUser() {
//        System.out.println("searchUser");
//        String email = "";
//        UserServiceImpl instance = null;
//        UserDto expResult = null;
//        UserDto result = instance.searchUser(email);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
    
}
