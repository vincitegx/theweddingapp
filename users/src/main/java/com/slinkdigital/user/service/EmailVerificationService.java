package com.slinkdigital.user.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import com.slinkdigital.user.repository.UserRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationService {

    @Value("${activation.token.expiration.time.hours}")
    private Long activationTokenExpirationTimeInHours;

    @Value("${organization.properties.mail}")
    private String organizationEmail;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final Clock clock = Clock.systemUTC();

    public String registerVerificationTokenToDb(UserDto userDto) {
        String generatedToken = UUID.randomUUID().toString();
        Users user = Users.builder().id(userDto.getId()).email(userDto.getEmail()).build();
        emailVerificationTokenRepository.findByUser(user).ifPresent(token -> emailVerificationTokenRepository.delete(token));
        EmailVerificationToken verificationToken = new EmailVerificationToken(generatedToken, user, LocalDateTime.now().plusHours(activationTokenExpirationTimeInHours));
        emailVerificationTokenRepository.save(verificationToken);
        Map<String, String> data = new HashMap<>();
        data.put("token", generatedToken);
        data.put("expiresAt", activationTokenExpirationTimeInHours.toString());
        EventDto eventDto = EventDto.builder().from(organizationEmail).to(userDto.getEmail()).data(data).build();
        kakfaTemplate.send("user-registration", eventDto);
        return generatedToken;
    }

    public void requestNewVerificationToken(EmailRequest emailRequest) {
        Users user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("This email has not been registered. Visit the registration page to register an account..."));
        UserDto userDto = userDtoMapper.apply(user);
        registerVerificationTokenToDb(userDto);
    }

    public Users verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(() -> new UserException("Token does not exist !!!"));
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now(clock))) {
            throw new TokenExpiredException("Token Expired !!!", verificationToken.getExpiresAt().toInstant(ZoneOffset.UTC));
        } else {
            Users user = verificationToken.getUser();
            user.setEnabled(true);
            user.setNonLocked(true);
            user = userRepository.saveAndFlush(user);
            emailVerificationTokenRepository.delete(verificationToken);
            return user;
        }
    }
}
