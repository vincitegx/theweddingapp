package com.slinkdigital.user.service.impl;

import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slinkdigital.user.repository.UserRepository;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        Users user = userOptional.orElseThrow(()-> new UsernameNotFoundException("User not found with email "+ email));
        validateLoginAttempt(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getIsEnabled(), true, true, user.getIsNonLocked(), getAuthorities(user));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }
    
    private void validateLoginAttempt(Users user) {
        if(user.getIsNonLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getEmail())) {
                user.setIsNonLocked(Boolean.FALSE);
            } else {
                user.setIsNonLocked(Boolean.TRUE);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }
}
