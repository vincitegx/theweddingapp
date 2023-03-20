package com.slinkdigital.user.security;

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.slinkdigital.user.domain.RefreshToken;
import com.slinkdigital.user.domain.Users;
import com.slinkdigital.user.domain.security.Role;
import com.slinkdigital.user.exception.UserException;
import com.slinkdigital.user.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import static java.util.Date.from;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@Data
@Slf4j
public class JwtProvider {

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @Value("${rt.expiration.time}")
    private Long refreshTokenExpirationInMillis;

    @Value("${project.name}")
    private String applicationName;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    private Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    public Long getRefreshTokenExpirationInMillis() {
        return refreshTokenExpirationInMillis;
    }

    public String generateJwtToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .withIssuer("WEDDING_APP")
                .withIssuedAt(Date.from(Instant.now()))
                .withClaim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(HMAC256(secret.getBytes()));
    }

    public String generateJwtToken(RefreshToken refreshToken) {
        try {
            User principal = (User) User.builder()
                    .username(refreshToken.getUser().getEmail())
                    .authorities(getAuthorities(refreshToken.getUser()))
                    .password(refreshToken.getUser().getPassword())
                    .disabled(refreshToken.getUser().getEnabled())
                    .accountExpired(false)
                    .accountLocked(refreshToken.getUser().getNonLocked())
                    .credentialsExpired(false)
                    .build();
            return JWT.create()
                    .withSubject(refreshToken.getUser().getEmail())
                    .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                    .withIssuer("WEDDING_APP")
                    .withIssuedAt(from(Instant.now()))
                    .withClaim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(HMAC256(secret.getBytes()));
        } catch (JWTCreationException | IllegalArgumentException ex) {
            throw new UserException(ex.getMessage());
        }
    }
}
