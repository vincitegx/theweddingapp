package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.PasswordResetToken;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@Disabled
@DataJpaTest(properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class PasswordResetTokenRepositoryTest {
    
    @Autowired
    private PasswordResetTokenRepository underTest;
    
    private final String TOKEN = "1234";
    
    @BeforeEach
    public void setUp(){
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .email("david@gmail.com")
                .resetToken(TOKEN)
                .createdAt(Instant.now())
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();
        underTest.save(passwordResetToken);
    }

    @AfterEach
    public void tearDown(){
        underTest.deleteAll();
    }
    /**
     * Test of findByResetToken method, of class PasswordResetTokenRepository.
     * @param given
     */
    @ParameterizedTest
    @CsvSource("1234")
    public void testFindByResetToken(String given) {
        Optional<PasswordResetToken> passwordResetToken = underTest.findByResetToken(given);
        assertThat(passwordResetToken).isNotEmpty();
    }
    
}
