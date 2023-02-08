package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.Users;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.function.Try.success;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository underTest;
    /**
     * Test of findByEmail method, of class UserRepository.
     */
    @Test
    public void testFindByEmail() {
        // given
        String email = "davidogbodu3056@gmail.com";
        // when
        Optional<Users> result = underTest.findByEmail(email);
        // then
        assertTrue(result.isPresent());
    }    
}
