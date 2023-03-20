package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.PasswordResetToken;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@DataJpaTest(properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class PasswordResetTokenRepositoryTest {
    
    @Autowired
    private PasswordResetTokenRepository underTest;
    
    @BeforeEach
    @Disabled
    public void setUp(){
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .email("david@gmail.com")
                .resetToken("token")
                .createdAt(Instant.now())
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();
        underTest.save(passwordResetToken);
    }

    @AfterEach
    @Disabled
    public void tearDown(){
        underTest.deleteAll();
    }
    /**
     * Test of findByResetToken method, of class PasswordResetTokenRepository.
     */
    @Test
    @Disabled
    public void testFindByResetToken() {
        //given
        String resetToken = "token";
        Optional<PasswordResetToken> passwordResetToken = underTest.findByResetToken(resetToken);
        assertThat(passwordResetToken).isPresent();
    }
    
}
