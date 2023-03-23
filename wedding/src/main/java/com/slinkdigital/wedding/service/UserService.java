package com.slinkdigital.wedding.service;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient.Builder webClientBuilder;
    private final HttpServletRequest request;

    public Boolean addRoleCouple(List<Long> couples) {
        return webClientBuilder.build().post()
                .uri("http://gateway-service/api/us/v1/users/roles/couple",
                        uriBuilder -> uriBuilder.queryParam("couple", couples).build())
                .header("authorization", request.getHeader(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}
