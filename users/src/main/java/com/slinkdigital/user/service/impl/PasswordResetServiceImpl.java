package com.slinkdigital.user.service.impl;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.PasswordResetToken;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.PasswordResetRequest;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.PasswordResetTokenRepository;
import com.slinkdigital.user.service.PasswordResetService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.slinkdigital.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    @Value("${password.reset.token.expiration.time.hours}")
    private Long passwordResetTokenExpirationTimeInHours;

    @Value("${organization.properties.mail}")
    private String organizationEmail;

    @Override
    public Map<String, String> generatePasswordResetToken(EmailRequest emailRequest) {
        try {
            Map<String, String> resetTokenGenerationStatus = new HashMap<>();
            Users user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + emailRequest.getEmail()));
            String resetToken = RandomStringUtils.random(4, false, true);
            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .email(user.getEmail())
                    .resetToken(resetToken)
                    .createdDate(Instant.now())
                    .expiresAt(LocalDateTime.now().plusHours(passwordResetTokenExpirationTimeInHours))
                    .build();
            passwordResetTokenRepository.save(passwordResetToken);
            Map<String, String> data = new HashMap<>();
            data.put("token", resetToken);
            data.put("expiresAt", passwordResetTokenExpirationTimeInHours.toString());
            EventDto eventDto = EventDto.builder().from(organizationEmail).to(emailRequest.getEmail()).data(data).build();
            kakfaTemplate.send("user-password-reset", eventDto);
            resetTokenGenerationStatus.put("success", "Password Reset Token Generated Successfully, Please Check Your Mail !!!");
            return resetTokenGenerationStatus;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> resetAccountPassword(PasswordResetRequest passwordResetRequest) {
        try {
            Map<String, String> resetAccountPasswordStatus = new HashMap<>();
            Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByResetToken(passwordResetRequest.getResetToken());
            if (!(passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword()))) {
                throw new UserException("Passwords Does'nt Match !!!");
            } else {
                if (passwordResetToken.isEmpty()) {
                    throw new UserException("No such token found !!!");
                } else if (passwordResetToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                    throw new UserException("Token is expired !!!");
                } else {
                    Optional<Users> user = userRepository.findByEmail(passwordResetToken.get().getEmail());
                    if (user.isEmpty()) {
                        throw new UserException("Email " + passwordResetToken.get().getEmail() + " is not registered with us !!!");
                    } else {
                        user.get().setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
                        userRepository.save(user.get());
                        passwordResetTokenRepository.delete(passwordResetToken.get());
                        resetAccountPasswordStatus.put("success", "Account password has been reset successfully !!! Login with your new credentials now !!!");
                    }
                }
            }
            return resetAccountPasswordStatus;
        } catch (UserException ex) {
            throw new UserException(ex.getMessage());
        }
    }
}
