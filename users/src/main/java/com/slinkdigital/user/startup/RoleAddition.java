package com.slinkdigital.user.startup;

import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
@Order(1)
public class RoleAddition implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
//        Role role = new Role("ROLE_USER");
//        roleRepository.save(role);
    }

}
