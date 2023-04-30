package com.slinkdigital.user.repository;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author TEGA
 */
@Disabled
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Autowired
    private RoleRepository roleRepository;

    private final String EMAIL = "david@gmail.com";

    @BeforeEach
    public void setUp() {
        Role role = new Role("ROLE_USER");
        role = roleRepository.save(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Users user = new Users(null, EMAIL, "1234", LocalDateTime.now(), true, true, roles);
        underTest.save(user);
    }

    @AfterEach
    public void tearDown() {
        underTest.deleteAll();
        roleRepository.deleteAll();
    }

    /**
     * Test of findByEmail method, of class UserRepository.
     */
    @Test
    @DisplayName("Test to find User by email...")
    public void testFindByEmail() {
        Optional<Users> result = underTest.findByEmail(EMAIL);
        assertTrue(result.isPresent());
        assertEquals(EMAIL, result.get().getEmail());
    }
}
