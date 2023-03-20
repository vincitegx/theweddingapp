package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.repository.EmailVerificationTokenRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @author TEGA
 */
@ExtendWith(MockitoExtension.class)
public class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private KafkaTemplate<String, EventDto> kafkaTemplate;

    private EmailVerificationService underTest;

    @Captor
    private ArgumentCaptor<UUID> tokenArgumentCaptor;
    /**
     * Test of registerVerificationTokenToDb method, of class
 EmailVerificationService.
     */

//    @BeforeEach
//    public void setUp() {
//        underTest = new EmailVerificationService(emailVerificationTokenRepository, kafkaTemplate);
//    }

    @Test
    @Disabled
    public void testRegisterVerificationTokenToDb() {
        String userEmail = "david@gmail.com";
//        Users user = Users.builder()
//                .id(123L) 
//                .email(userEmail)
//                .build();
//        UserDto userDto = UserDto.builder()
//                .email(userEmail)
//                .id(123L)
//                .build();
//        underTest.registerVerificationTokenToDb(userDto);
//        Mockito.verify(emailVerificationTokenRepository).findByUser(user);
//        Mockito.verify(emailVerificationTokenRepository).save(ArgumentMatchers.any(EmailVerificationToken.class));
    }
}
