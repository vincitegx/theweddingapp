package com.slinkdigital.user.service;

import com.slinkdigital.user.constant.DefaultRoles;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import com.slinkdigital.user.repository.RoleRepository;
import com.slinkdigital.user.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserDto setDefaultRole(Users user) {
        Role role = roleRepository.findByName(DefaultRoles.DEFAULT_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.saveAndFlush(user);
        return userDtoMapper.apply(user);
    }

    public void addRoleCouple(List<Long> couple) {
        Role role = roleRepository.findByName(DefaultRoles.ROLE_COUPLE);
        couple.forEach((c) -> {
            Users user = userRepository.findById(c).orElseThrow(() -> new UserException("No user associated with this id:" + c));
            user.getRoles().add(role);
            userRepository.save(user);
        });
    }
}
