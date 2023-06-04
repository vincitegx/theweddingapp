package com.slinkdigital.user.service;

import com.slinkdigital.user.constant.DefaultRoles;
import com.slinkdigital.user.dto.CustomUserDetails;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.repository.RoleRepository;
import com.slinkdigital.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }
        
        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        Users user = upsertUser(customUserDetails);
        customUserDetails.setId(user.getId());
        return customUserDetails;
        
        
//        String email = oAuth2User.getAttribute("email");
//        Optional<Users> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            return oAuth2User;
//        } else {
//            Set<Role> roles = new HashSet<>();
//            Role role = roleRepository.findByName(DefaultRoles.DEFAULT_ROLE);
//            roles.add(role);
//            String password = UUID.randomUUID().toString();
//            Users user = new Users();
//            user.setEmail(email);
//            user.setCreatedAt(LocalDateTime.now());
//            user.setEnabled(true);
//            user.setNonLocked(true);
//            user.setRoles(roles);
//            user.setPassword(passwordEncoder.encode(password));
//            userRepository.save(user);
//            return oAuth2User;
//        }
    }
    
    private Users upsertUser(CustomUserDetails customUserDetails) {
        Optional<Users> userOptional = userRepository.findByEmail(customUserDetails.getEmail());
        Users user;
        if (userOptional.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName(DefaultRoles.DEFAULT_ROLE);
            roles.add(role);
            String password = UUID.randomUUID().toString();
            user = new Users();
            user.setEmail(customUserDetails.getEmail());
            user.setProvider(customUserDetails.getProvider());
            user.setCreatedAt(LocalDateTime.now());
            user.setEnabled(true);
            user.setNonLocked(true);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(password));
        } else {
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
        }
        return userRepository.saveAndFlush(user);
    }
}
