package com.slinkdigital.apigateway.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.slinkdigital.apigateway.dto.ErrorResponse;
import com.slinkdigital.apigateway.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

/**
 *
 * @author TEGA
 */
@RefreshScope
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    private static final String GOOGLE_ISSUER = "https://accounts.google.com";
    private static final String GOOGLE_AUDIENCE = "your-audience";

    private static final int MIN_TOKEN_LENGTH = 100; // Example minimum token length
    private static final int MAX_TOKEN_LENGTH = 1000;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization information");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String[] parts = authHeader.split(" ");

                if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                    throw new RuntimeException("Incorrect authorization structure");
                }

                String token = parts[1];
                // Check if the token is an OAuth2 token
                if (isOAuth2Token(token)) {
                    // Handle OAuth2 authentication
                    return handleOAuth2Authentication(token, exchange, chain);
                } else {
                    // Handle custom login authentication
                    return handleCustomLoginAuthentication(token, exchange, chain);
                }
                return webClientBuilder.build()
                        .post()
                        .uri("http://gateway-service/api/uu/v1/users/token/validate?token=" + parts[1])
                        .retrieve().bodyToMono(UserDto.class)
                        .map(user -> {
                            exchange.getRequest()
                                    .mutate()
                                    .header("x-id", String.valueOf(user.getId()))
                                    .header("x-email", String.valueOf(user.getEmail()))
                                    .header("x-roles", user.getRoles().toString());
                            return exchange;
                        }).flatMap(chain::filter);

            } catch (RuntimeException ex) {
                log.error("Error validating authentication header", ex);
                List<String> details = new ArrayList<>();
                details.add(ex.getLocalizedMessage());
                ErrorResponse error = new ErrorResponse(new Date(), HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), details, exchange.getRequest().getURI().toString());
                ServerHttpResponse response = exchange.getResponse();
                byte[] bytes = SerializationUtils.serialize(error);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                response.writeWith(Flux.just(buffer));
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (ParseException ex) {
                Logger.getLogger(AuthFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }

    private Mono<Void> handleOAuth2Authentication(String token, ServerWebExchange exchange, GatewayFilterChain chain) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-microservice/oauth2/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    // Handle 4xx client errors
                    log.error("Error validating OAuth2 authentication token: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Invalid OAuth2 authentication token"));
                })
                .bodyToMono(UserInfo.class)
                .map(userInfo -> {
                    exchange.getRequest()
                            .mutate()
                            .header("x-id", String.valueOf(userInfo.getId()))
                            .header("x-email", String.valueOf(userInfo.getEmail()))
                            .header("x-roles", userInfo.getRoles().toString());
                    return exchange;
                })
                .flatMap(chain::filter);
    }

    private Mono<Void> handleCustomLoginAuthentication(String token, ServerWebExchange exchange, GatewayFilterChain chain) {
        return webClientBuilder.build()
                .post()
                .uri("http://user-microservice/api/uu/v1/users/token/validate?token=" + token)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    // Handle 4xx client errors
                    log.error("Error validating custom login authentication token: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Invalid custom login authentication token"));
                })
                .bodyToMono(UserDto.class)
                .map(user -> {
                    exchange.getRequest()
                            .mutate()
                            .header("x-id", String.valueOf(user.getId()))
                            .header("x-email", String.valueOf(user.getEmail()))
                            .header("x-roles", user.getRoles().toString());
                    return exchange;
                })
                .flatMap(chain::filter);
    }

    private boolean isOAuth2Token(String token) throws java.text.ParseException {
        // Perform robust validation for Google OAuth2 tokens
        // Example validation criteria:
        // 1. Check token length
        // 2. Validate token format (e.g., base64-encoded JWT)
        // 3. Check for specific token claims (e.g., issuer, audience)
        SignedJWT signedJWT = SignedJWT.parse(token);
        // Verify signature and token claims if needed
        // Example: Verify signature with Google's public keys

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        // Check issuer claim
        String issuer = claimsSet.getIssuer();
        if (issuer == null || !issuer.equals(GOOGLE_ISSUER)) {
            return false;
        }
        // Check audience claim
        List<String> audiences = claimsSet.getAudience();
        if (audiences == null || !audiences.contains(GOOGLE_AUDIENCE)) {
            return false;
        }
        // Additional checks if necessary...

        return true;
    }

    public CompletableFuture<String> fallbackMethod(Config config, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please try again later!");
    }

    public static class Config {
    }
}
