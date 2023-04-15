package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.PasswordResetToken;
import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.PasswordResetRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.PasswordResetTokenRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.slinkdigital.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
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
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    private final HttpServletRequest request;
    private final Clock clock;

    @Value("${password.reset.token.expiration.time.hours}")
    private Long passwordResetTokenExpirationTimeInHours;

    @Value("${organization.properties.mail}")
    private String organizationEmail;

    public void generatePasswordResetToken(EmailRequest emailRequest) {
        Users user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + emailRequest.getEmail()));
        String resetToken = RandomStringUtils.random(4, false, true);
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .email(user.getEmail())
                .resetToken(resetToken)
                .createdAt(Instant.now())
                .expiresAt(LocalDateTime.now().plusHours(passwordResetTokenExpirationTimeInHours))
                .build();
        passwordResetTokenRepository.save(passwordResetToken);
        Map<String, String> data = new HashMap<>();
        data.put("token", resetToken);
        data.put("expiresAt", passwordResetTokenExpirationTimeInHours.toString());
        EventDto eventDto = EventDto.builder().from(organizationEmail).to(emailRequest.getEmail()).data(data).build();
        kakfaTemplate.send("user-password-reset", eventDto);
    }

    public void resetAccountPassword(PasswordResetRequest passwordResetRequest) {
        if (!(passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword()))) {
            throw new UserException("Passwords Does'nt Match !!!");
        } else {
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByResetToken(passwordResetRequest.getResetToken()).orElseThrow(() -> new UserException("No such token found !!!"));
            if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now(clock))) {
                throw new UserException("Token is expired !!!");
            } else {
                Users user = userRepository.findByEmail(passwordResetToken.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found with email"));
                user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
                userRepository.save(user);
                passwordResetTokenRepository.delete(passwordResetToken);
            }
        }
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Users user = userRepository.findById(getLoggedInUserId()).orElseThrow(() -> new UsernameNotFoundException("Cannot Identify The User, Therefore operation cannot be performed"));
        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new UserException("Current password field is incorrect");
        } else if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            throw new UserException("New password doesnt match confirm password field");
        } else {
            user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.saveAndFlush(user);
        }
    }

    private Long getLoggedInUserId() {
        String xid = request.getHeader("x-id");
        return Long.parseLong(xid);
    }
}
