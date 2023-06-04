package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 *
 * @author TEGA
 */
public interface OAuth2UserInfoExtractor {
    
    CustomUserDetails extractUserInfo(OAuth2User oAuth2User);

    boolean accepts(OAuth2UserRequest userRequest);
    
}
