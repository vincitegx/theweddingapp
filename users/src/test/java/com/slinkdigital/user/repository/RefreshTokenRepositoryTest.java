package com.slinkdigital.user.repository;

import com.slinkdigital.user.domain.RefreshToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@DataJpaTest(properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class RefreshTokenRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RefreshTokenRepository underTest;
    
    @BeforeEach
    @Disabled
    public void setUp(){
        Role role = new Role("ROLE_USER");
        role = roleRepository.save(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Users user = new Users(null, "david@gmail.com", "1234",LocalDateTime.now(), true, true, roles);      
        user = userRepository.save(user);
        RefreshToken refreshToken = new RefreshToken(null, "token", user, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        underTest.save(refreshToken);
    }

    @AfterEach
    @Disabled
    public void tearDown(){
        underTest.deleteAll();
    }
    
    /**
     * Test of findByToken method, of class RefreshTokenRepository.
     */
    @Test
    @Disabled
    @DisplayName("Find Refresh Token Object By token")
    public void testFindByToken() {
        //given
        String token = "token";
        Optional<RefreshToken> refreshToken = underTest.findByToken(token);
        assertThat(refreshToken).isPresent();
    }

    /**
     * Test of deleteByToken method, of class RefreshTokenRepository.
     */
    @Test
    @Disabled
    public void testDeleteByToken() {
        String token = "token";
        underTest.deleteByToken(token);
        assertThat(underTest.findByToken(token)).isEmpty();
    }
}
