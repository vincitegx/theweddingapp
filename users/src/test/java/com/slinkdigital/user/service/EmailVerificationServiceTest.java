package com.slinkdigital.user.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.javafaker.Faker;
import com.slinkdigital.user.constant.DefaultRoles;
import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import com.slinkdigital.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @author TEGA
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private KafkaTemplate<String, EventDto> kafkaTemplate;

    @Mock(lenient = true)
    private UserRepository userRepository;
    
    @Mock(lenient = true)
    private Clock clock;

    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    private EmailVerificationService underTest;
    
    private static final ZonedDateTime NOW = ZonedDateTime.of(2023, 4, 20, 4, 55, 50, 0, ZoneId.of("GMT"));

    /**
     * Test of registerVerificationTokenToDb method, of class
     * EmailVerificationService.
     */
    @BeforeEach
    public void setUp() {
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());
        underTest = new EmailVerificationService(emailVerificationTokenRepository, kafkaTemplate, userRepository, userDtoMapper);
    }

    @Test
    @Order(1)
    void testRegisterVerificationTokenToDb() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = underTest.getClass().getDeclaredField("activationTokenExpirationTimeInHours");
        field.setAccessible(true);
        field.set(underTest, 1L);
        Faker f = new Faker();
        String userEmail = f.internet().safeEmailAddress();
        Users user = Users.builder()
                .id(123L)
                .email(userEmail)
                .build();
        UserDto userDto = UserDto.builder()
                .email(userEmail)
                .id(123L)
                .build();
        underTest.registerVerificationTokenToDb(userDto);
        verify(emailVerificationTokenRepository).findByUser(user);
        verify(emailVerificationTokenRepository).save(ArgumentMatchers.any(EmailVerificationToken.class));
        
        
     
//    User user = new User();
//    user.setEmail("test@example.com");
//    user.setId(1L);
//
//    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//    underTest.registerVerificationTokenToDb(user.getId());
//
//    ArgumentCaptor<EmailVerificationToken> tokenCaptor = ArgumentCaptor.forClass(EmailVerificationToken.class);
//    verify(emailVerificationTokenRepository).save(tokenCaptor.capture());
//    EmailVerificationToken token = tokenCaptor.getValue();
//    assertNotNull(token.getToken());
//    assertEquals(user, token.getUser());
//    assertEquals(NOW.plusHours(24), token.getExpiryDate());
    }

    @Test
    @Order(2)
    void requestNewVerificationToken() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field field = underTest.getClass().getDeclaredField("activationTokenExpirationTimeInHours");
        field.setAccessible(true);
        field.set(underTest, 1L);
        Faker f = new Faker();
        String userEmail = f.internet().safeEmailAddress();
        EmailRequest emailRequest = new EmailRequest(userEmail);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(DefaultRoles.DEFAULT_ROLE));
        Users user = Users.builder()
                .id(123L)
                .email(emailRequest.getEmail())
                .roles(roles)
                .build();
        UserDto userDto = userDtoMapper.apply(user);
        underTest.registerVerificationTokenToDb(userDto);
    }

    @Test
    @Order(3)
    void willThrownWhenTryingToVerifyEmailWhenTokenDoNotExists() {
        String token = UUID.randomUUID().toString();
        assertThatThrownBy(() -> underTest.verifyEmail(token))
                .isInstanceOf(UserException.class)
                .hasMessage("Token does not exist !!!");
        verify(emailVerificationTokenRepository, never()).save(ArgumentMatchers.any());
    }
    
    @Test
    @Order(4)
    void verifyEmailFailsWhenTokenIsExpired() {
        Faker f = new Faker();
        String userEmail = f.internet().safeEmailAddress();
        String token = UUID.randomUUID().toString();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(DefaultRoles.DEFAULT_ROLE));
        Users user = Users.builder()
                .id(123L)
                .email(userEmail)
                .roles(roles)
                .build();
        LocalDateTime expiresAt = LocalDateTime.of(2023, 4, 20, 4, 55, 49);
        EmailVerificationToken evt = new EmailVerificationToken(123L, token, user, LocalDateTime.now(), expiresAt);
        when(emailVerificationTokenRepository.findByToken(token)).thenReturn(Optional.of(evt));
        assertThatThrownBy(() -> underTest.verifyEmail(token))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessage("Token Expired !!!");
        verify(emailVerificationTokenRepository, never()).save(ArgumentMatchers.any());
    }
    
    @Test
    @Order(5)
    void verifyEmail() {
        Faker f = new Faker();
        String userEmail = f.internet().safeEmailAddress();
        String token = UUID.randomUUID().toString();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(DefaultRoles.DEFAULT_ROLE));
        Users user = Users.builder()
                .id(123L)
                .email(userEmail)
                .roles(roles)
                .build();
        LocalDateTime expiresAt = LocalDateTime.of(2023, 4, 20, 4, 55, 51);
        EmailVerificationToken evt = new EmailVerificationToken(123L, token, user, LocalDateTime.now(), expiresAt);
        when(emailVerificationTokenRepository.findByToken(token)).thenReturn(Optional.of(evt));
        underTest.verifyEmail(token);
        verify(userRepository).saveAndFlush(user);
        verify(emailVerificationTokenRepository).delete(evt);
    }

}
