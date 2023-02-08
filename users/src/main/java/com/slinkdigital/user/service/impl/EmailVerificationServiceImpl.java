package com.slinkdigital.user.service.impl;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import com.slinkdigital.user.service.EmailVerificationService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Value("${activation.token.expiration.time.hours}")
    private Long activationTokenExpirationTimeInHours;

    @Value("${organization.properties.mail}")
    private String organizationEmail;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    @Override
    public String registerVerificationTokenToDb(UserDto userDto) {
        try {
            String generatedToken = UUID.randomUUID().toString();
            Users user = Users.builder()
                    .email(userDto.getEmail())
                    .id(userDto.getId())
                    .build();
            Optional<EmailVerificationToken> emailVerificationToken = emailVerificationTokenRepository.findByUser(user);
            if (emailVerificationToken.isPresent()) {
                emailVerificationTokenRepository.delete(emailVerificationToken.get());
            }
            EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                    .token(generatedToken)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(activationTokenExpirationTimeInHours))
                    .user(user)
                    .build();
            emailVerificationTokenRepository.save(verificationToken);
            Map<String, String> data = new HashMap<>();
            data.put("token", generatedToken);
            data.put("expiresAt", activationTokenExpirationTimeInHours.toString());
            EventDto eventDto = EventDto.builder().from(organizationEmail).to(userDto.getEmail()).data(data).build();
            kakfaTemplate.send("user-registration", eventDto);
            return generatedToken;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
}
