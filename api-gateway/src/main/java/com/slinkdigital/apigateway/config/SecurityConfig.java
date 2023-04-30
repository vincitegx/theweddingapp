package com.slinkdigital.apigateway.config;

import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.csrf().disable()
//                .authorizeExchange()
//                .pathMatchers("/api/**")
//                .permitAll()
//                .and()
//                .authorizeExchange()
//                .pathMatchers("/webjars/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
//                .authenticated().and().httpBasic().and().build();
//        
        return httpSecurity
                .csrf().disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/**").permitAll()
                        .pathMatchers("/webjars/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html").authenticated())
                .httpBasic().and()
                .build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user1")
                .password("password")
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities("user:read")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
