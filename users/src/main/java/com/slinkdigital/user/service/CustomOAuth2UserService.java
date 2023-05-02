package com.slinkdigital.user.service;

import com.slinkdigital.user.constant.DefaultRoles;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.repository.RoleRepository;
import com.slinkdigital.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return oAuth2User;
        } else {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName(DefaultRoles.DEFAULT_ROLE);
            roles.add(role);
            String password = UUID.randomUUID().toString();
            Users user = new Users();
            user.setEmail(email);
            user.setCreatedAt(LocalDateTime.now());
            user.setEnabled(true);
            user.setNonLocked(true);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return oAuth2User;
        }
    }
}
