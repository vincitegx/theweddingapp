package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.mapper.UserDtoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slinkdigital.user.repository.UserRepository;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userDtoMapper).toList();
    }
}
