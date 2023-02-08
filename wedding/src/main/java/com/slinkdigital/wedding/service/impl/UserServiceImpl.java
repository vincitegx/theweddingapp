package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class UserServiceImpl implements UserService{

    private final WebClient.Builder webClientBuilder;
    private final HttpServletRequest request;

    @Override
    public Boolean addRoleCouple(List<Long> couples) {
        return webClientBuilder.build().post()
                .uri("http://API-GATEWAY/api/us/v1/users/role-couple",
                        uriBuilder -> uriBuilder.queryParam("couple", couples).build())
                .header("authorization", request.getHeader(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}
