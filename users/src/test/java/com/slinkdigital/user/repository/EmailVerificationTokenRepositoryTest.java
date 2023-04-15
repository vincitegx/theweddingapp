package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.EmailVerificationToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private final String TOKEN = "1234";

    /**
     * Test of findByToken method, of class EmailVerificationTokenRepository.
     */
    @BeforeEach
    public void setUp() {
        Role role = new Role("ROLE_USER");
        role = roleRepository.save(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Users user = new Users(null, "david@gmail.com", "1234",LocalDateTime.now(), true, true, roles);                 
        user = userRepository.save(user);
        EmailVerificationToken evt = new EmailVerificationToken(TOKEN, user, LocalDateTime.now().plusDays(1));
        underTest.save(evt);
    }

    @AfterEach
    public void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Test to find Email Verification Object By Token")
    public void testFindByToken() {
        Optional<EmailVerificationToken> result = underTest.findByToken(TOKEN);
        assertTrue(result.isPresent());
    }

    /**
     * Test of findByUser method, of class EmailVerificationTokenRepository.
     */
    @Test
    @DisplayName("Test to find Email Verification Object By User")
    public void testFindByUser() {
        //Given
        Users user = userRepository.findByEmail("david@gmail.com").get();
        Optional<EmailVerificationToken> result = underTest.findByUser(user);
        assertTrue(result.isPresent());
    }
}
