package com.slinkdigital.user.service;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.mapper.UserDtoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slinkdigital.user.repository.UserRepository;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public List<UserDto> getAllUsers() {
        try {
            return userRepository.findAll().stream().map(userDtoMapper).toList();
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    public UserDto searchUser(String email) {
        try {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + email));
            UserDto userDto = userDtoMapper.apply(user);
            return userDto;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
}
