package com.slinkdigital.user.repository;

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
@DataJpaTest
@Disabled
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository underTest;

    @BeforeEach
    public void setUp(){
        Set role = new HashSet<>();
        role.add("ROLE_USER");
//        Users user = Users.builder()
//                .email("davidogbodu3056@gmail.com")
//                .password("1234")
//                .enabled(Boolean.FALSE)
//                .locked(Boolean.FALSE)
//                .roles(role)
//                .createdAt(LocalDateTime.now())
//                .build();
//        user = underTest.save(user);
//        System.out.println(user);
    }
    
    @AfterEach
    public void tearDown(){
        underTest.deleteAll();
    }
    
    /**
     * Test of findByEmail method, of class UserRepository.
     */
    @Test
    @DisplayName("Test to find User by email...")
    public void testFindByEmail() {
        String email = "davidogbodu3056@gmail.com";
        Optional<Users> result = underTest.findByEmail(email);
        assertTrue(result.isPresent());
        
        // Given
//        Users user = new Users();
//        user.setEmail("test@example.com");
//        underTest.save(user);

        // When
//        Optional<Users> result = userRepository.findByEmail("test@example.com");

        // Then
//        assertTrue(result.isPresent());
//        assertEquals(user.getEmail(), result.get().getEmail());
    }
}
