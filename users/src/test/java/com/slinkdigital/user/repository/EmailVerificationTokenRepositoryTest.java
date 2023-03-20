package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.domain.Users;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@DataJpaTest(properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        })
public class EmailVerificationTokenRepositoryTest {

    @Autowired
    private EmailVerificationTokenRepository underTest;

    @Autowired
    private UserRepository userRepository;

    /**
     * Test of findByToken method, of class EmailVerificationTokenRepository.
     */
    @BeforeEach
    @Disabled
    public void setUp() {
        Set role = new HashSet<>();
        role.add("ROLE_USER");
//        Users expected = Users.builder()
//                .email("david@gmail.com")
//                .password("1234")
//                .enabled(true)
//                .locked(true)
//                .roles(role)
//                .createdAt(LocalDateTime.now())
//                .build();               
//        expected = userRepository.save(expected);
//        EmailVerificationToken evt = new EmailVerificationToken("token", expected, LocalDateTime.now().plusDays(1));
//        underTest.save(evt);
    }

    @AfterEach
    @Disabled
    public void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @Disabled
    @DisplayName("Test to find Email Verification Object By Token")
    public void testFindByToken() {
        String token = "token";
        Optional<EmailVerificationToken> result = underTest.findByToken(token);
        assertTrue(result.isPresent());
    }

    /**
     * Test of findByUser method, of class EmailVerificationTokenRepository.
     */
    @Test
    @Disabled
    @DisplayName("Test to find Email Verification Object By User")
    public void testFindByUser() {
        //Given
        Users user = userRepository.findByEmail("david@gmail.com").get();
        Optional<EmailVerificationToken> result = underTest.findByUser(user);
        assertTrue(result.isPresent());
    }
}
