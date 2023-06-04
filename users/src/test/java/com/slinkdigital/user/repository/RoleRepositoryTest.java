package com.slinkdigital.user.repository;

import com.slinkdigital.user.config.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import com.slinkdigital.user.domain.security.Role;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 *
 * @author TEGA
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {AbstractTestcontainers.Initializer.class})
public class RoleRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private RoleRepository underTest;

    @BeforeEach
    public void setUp() {
//        List<String> roles = new ArrayList<>();
//        roles.add("ROLE_USER");
//        roles.add("ROLE_COUPLE");
//        roles.add("ROLE_VENDOR");
//        roles.add("ROLE_ADMIN");
//        roles.forEach(r -> {
//            Role role = new Role("super-admin");
//            role.setName(r);
//            underTest.save(role);
//        });
//        
        Role role = new Role("super-admin");
        underTest.save(role);
    }

    @AfterEach
    public void tearDown() {
        underTest.deleteAll();
    }

    /**
     * Test of findByName method, of class RoleRepository.
     */
    @Test
    public void testFindByName() {
        //given
        String roleName = "super-admin";
        //when
        Role result = underTest.findByName(roleName);

        //then
        Assertions.assertThat(result).extracting("name").isEqualTo(roleName);
    }
}
