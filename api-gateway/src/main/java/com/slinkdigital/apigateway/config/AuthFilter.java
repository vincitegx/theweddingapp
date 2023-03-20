package com.slinkdigital.apigateway.config;

import com.slinkdigital.apigateway.dto.ErrorResponse;
import com.slinkdigital.apigateway.dto.UserDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 *
 * @author TEGA
 */
@RefreshScope
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
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
            }
        };
    }

    public static class Config {
    }
}
