package com.slinkdigital.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import com.slinkdigital.user.mapper.UserRegistrationMapper;
import com.slinkdigital.user.repository.UserRepository;
import com.slinkdigital.user.security.JwtProvider;
import com.slinkdigital.user.validator.EmailValidator;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailValidator emailValidator;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserRegistrationMapper userRegistrationMapper;

    public UserDto registerUser(RegisterRequest registerRequest) {
        if (!emailValidator.test(registerRequest.getEmail())) {
            throw new UserException(registerRequest.getEmail() + " is not valid");
        } else if (!(registerRequest.getPassword().equals(registerRequest.getConfirmPassword()))) {
            throw new UserException("Passwords Doesn't Match");
        } else if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserException("Email has already been used");
        } else {
            Users user = userRegistrationMapper.apply(registerRequest);
            user = userRepository.save(user);
            return userDtoMapper.apply(user);
        }
    }

    public JwtAuthResponse login(LoginRequest loginRequest) throws AuthenticationException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateJwtToken(authenticate);
        Users user = getCurrentUser();
        return JwtAuthResponse.builder()
                .authToken(token)
                .user(userDtoMapper.apply(user))
                .refreshToken(refreshTokenService.generateRefreshToken(user))
                .build();
    }

    @Cacheable(cacheNames = "userdto")
    public UserDto validateToken(String token) throws JWTVerificationException, IllegalArgumentException {
        Algorithm algorithm = HMAC256(jwtProvider.getSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("WEDDING_APP").build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String email = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication2.isAuthenticated()) {
            Users user = userRepository.findByEmail(email).get();
            UserDto userDto = userDtoMapper.apply(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userDto;
        } else {
            SecurityContextHolder.clearContext();
            authentication.setAuthenticated(false);
            throw new UserException("This user does not exist or is not logged in");
        }
    }

    @Transactional(readOnly = true)
    public Users getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No User not found with such email"));
    }

    @CacheEvict(cacheNames = "userdto")
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        SecurityContextHolder.clearContext();
    }
}
