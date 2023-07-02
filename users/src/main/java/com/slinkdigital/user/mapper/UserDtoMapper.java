package com.slinkdigital.user.mapper;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.UserDto;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
public class UserDtoMapper implements Function<Users, UserDto> {

    @Override
    public UserDto apply(Users user) {
        Set<String> roles = getUserRoles(user);
        return UserDto.builder().email(user.getEmail()).id(user.getId()).roles(roles).build();
    }

    private Set<String> getUserRoles(Users user) {
        return user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
    }
}
