package com.slinkdigital.user.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.slinkdigital.user.constant.DefaultRoles;
import static com.slinkdigital.user.constant.SecurityConstant.WEDDING_APP;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.domain.RefreshToken;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.JwtAuthResponse;
import com.slinkdigital.user.dto.LoginRequest;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.RegisterRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.dto.UserRequest;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import com.slinkdigital.user.repository.RefreshTokenRepository;
import com.slinkdigital.user.repository.RoleRepository;
import com.slinkdigital.user.security.JwtProvider;
import com.slinkdigital.user.service.EmailVerificationService;
import com.slinkdigital.user.service.RefreshTokenService;
import com.slinkdigital.user.validator.EmailValidator;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slinkdigital.user.repository.UserRepository;
import com.slinkdigital.user.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import static java.util.Arrays.stream;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailValidator emailValidator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpServletRequest request;
    @Value("${organization.properties.mail}")
    private String organizationEmail;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    @Override
    public Map<String, String> registerUser(RegisterRequest registerRequest) {
        try {
            Map<String, String> registrationStatus = new HashMap<>();
            if (!emailValidator.test(registerRequest.getEmail())) {
                throw new UserException(registerRequest.getEmail() + " is not valid");
            } else if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                throw new UserException("Email has already been used");
            } else if (!(registerRequest.getPassword().equals(registerRequest.getConfirmPassword()))) {
                throw new UserException("Passwords Doesn't Match");
            } else {
                Users user = Users.builder()
                        .email(registerRequest.getEmail())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .createdAt(LocalDateTime.now())
                        .build();
                user = userRepository.saveAndFlush(user);
                UserDto userDto = mapUserToDto(user);
                String token = emailVerificationService.registerVerificationTokenToDb(userDto);
                registrationStatus.put("success", "Thank you for signing up, A verification link has been sent to your email " + userDto.getEmail() + " : " + token);
            }
            return registrationStatus;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> verifyEmail(String token) {
        try {
            Map<String, String> verificationStatus = new HashMap<>();
            Optional<EmailVerificationToken> verificationToken = emailVerificationTokenRepository.findByToken(token);
            if (verificationToken.isEmpty()) {
                throw new UserException("Token does not exist !!!");
            } else if (verificationToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new TokenExpiredException("Token Expired !!!");
            } else {
                EmailVerificationToken v = verificationToken.get();
                fetchUserEnableAndAssignDefaultRole(v);
                emailVerificationTokenRepository.delete(v);
                verificationStatus.put("success", "Congratulations !!! Your email has been verified and your account activated. You can log in to your account...");
            }
            return verificationStatus;
        } catch (TokenExpiredException | UserException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    private void fetchUserEnableAndAssignDefaultRole(EmailVerificationToken emailVerificationToken) {
        try {
            String email = emailVerificationToken.getUser().getEmail();
            Users user = findByEmail(email);
            user.setIsEnabled(true);
            user.setIsNonLocked(true);
            user.setRoles(setDefaultRole());
            userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> requestNewVerificationToken(EmailRequest emailRequest) {
        try {
            Map<String, String> requestStatus = new HashMap<>();
            if (!emailValidator.test(emailRequest.getEmail())) {
                throw new UserException(emailRequest.getEmail() + " is not valid");
            } else if (userRepository.findByEmail(emailRequest.getEmail()).isEmpty()) {
                throw new UsernameNotFoundException("This email has not been registered. Visit the registration page to register an account...");
            } else {
                Users user = userRepository.findByEmail(emailRequest.getEmail()).get();
                UserDto userDto = mapUserToDto(user);
                String token = emailVerificationService.registerVerificationTokenToDb(userDto);
                requestStatus.put("success", "A new verification link has been sent to your email " + userDto.getEmail() + " : " + token);
            }
            return requestStatus;
        } catch (UserException | UsernameNotFoundException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtProvider.generateJwtToken(authenticate);
            Users user = getCurrentUser();
            return JwtAuthResponse.builder()
                    .authToken(token)
                    .user(mapUserToDto(user))
                    .refreshToken(refreshTokenService.generateRefreshToken(user))
                    .build();
        } catch (AuthenticationException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    @Cacheable(cacheNames = "userdto")
    public UserDto validateToken(String token) {
        try {
            Algorithm algorithm = HMAC256(jwtProvider.getSecret().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(WEDDING_APP).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            boolean isExpired = decodedJWT.getExpiresAt().before(new Date());
            String email = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
                    null, authorities);
            Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated() && authentication2.isAuthenticated()) {
                Users user = userRepository.findByEmail(email).get();
                UserDto userDto = mapUserToDto(user);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return userDto;
            } else {
                SecurityContextHolder.clearContext();
                authentication.setAuthenticated(false);
                throw new UserException("This user does not exist or is not logged in");
            }
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Users getCurrentUser() {
        try {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal();
            Users user = findByEmail(principal.getUsername());
            return user;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
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

    private RefreshToken validateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                    .orElseThrow(() -> new UserException("Invalid refresh Token"));
            if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
                refreshTokenRepository.delete(refreshToken);
                throw new UserException("Refresh token was expired. Please make a new signin request");
            }
            UserDto user = mapUserToDto(refreshToken.getUser());
            if (!user.equals(refreshTokenRequest.getUser())) {
                throw new UserException("You will need to login again");
            }
            return refreshToken;
        } catch (UserException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    @CacheEvict(cacheNames = "userdto")
    public Boolean logout(RefreshTokenRequest refreshTokenRequest) {
        try {
            SecurityContextHolder.clearContext();
            return refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
    
    @Override
    public Map<String, String> sendMessageToAdmin(UserRequest userRequest) {
        try{
            Map<String, String> data = new HashMap<>();
            data.put("message", userRequest.getMessage());
            EventDto eventDto = EventDto.builder().from(userRequest.getEmail()).to(organizationEmail).data(data).build();
            kakfaTemplate.send("email-to-admin", eventDto);
            return data;
        }catch(RuntimeException ex){
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        try{
            Map<String, String> updateStatus = new HashMap<>();
            Users user = userRepository.findById(Long.parseLong(getLoggedInUserId())).orElseThrow(()-> new UserException("Cannot Identify The User, Therefore operation cannot be performed"));
            if(!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())){
                throw new UserException("Current password field is incorrect");
            }else if(!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())){
                throw new UserException("New password doesnt match confirm password field");
            }else{
                user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
                userRepository.saveAndFlush(user);
                updateStatus.put("success", "Password updated successfully");
            }
            return updateStatus;
        }catch(UserException ex){
            throw new UserException(ex.getMessage());
        }
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        try {
            return userRepository.findAll().stream().map(this::mapUserToDto).toList();
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    private Set<Role> setDefaultRole() {
        try {
            Role role = roleRepository.findByName(DefaultRoles.DEFAULT_ROLE);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            return roles;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public UserDto searchUser(String email) {
        try {
            Users user = findByEmail(email);
            UserDto userDto = mapUserToDto(user);
            return userDto;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    private Users findByEmail(String email) {
        try {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + email));
            return user;
        } catch (UsernameNotFoundException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public UserDto mapUserToDto(Users user) {
        try {
            Set<String> roles = getUserRoles(user);
            UserDto userDto = UserDto.builder().email(user.getEmail()).id(user.getId()).roles(roles).build();
            return userDto;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    private Set<String> getUserRoles(Users user) {
        try {
            Set<String> roles = new HashSet<>();
            if (user.getRoles().isEmpty()) {
                return roles;
            } else {
                user.getRoles().forEach((role) -> {
                    roles.add(role.getName());
                });
                return roles;
            }
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public void addRoleCouple(List<Long> couple) {
        try {
            Role role = roleRepository.findByName(DefaultRoles.ROLE_COUPLE);
            couple.forEach((c) -> {
                Users user = userRepository.findById(c).orElseThrow(()-> new UserException("No user associated with this id:"+ c));
                user.getRoles().add(role);
                userRepository.save(user);
            });
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
    
    private String getLoggedInUserId() {
        try {
            return request.getHeader("x-id");
        } catch (RuntimeException ex) {
            throw new UserException("You Need To Be Logged In !!!");
        }
    }
}
