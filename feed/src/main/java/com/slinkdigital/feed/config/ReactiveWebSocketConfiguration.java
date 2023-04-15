package com.slinkdigital.feed.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

/**
 *
 * @author TEGA
 */
//@Configuration
//public class ReactiveWebSocketConfiguration {
//
//    @Autowired
//    private WebSocketHandler webSocketHandler;
//
//    @Bean
//    public HandlerMapping webSocketHandlerMapping() {
//        Map<String, WebSocketHandler> urlMap = new HashMap<>();
//        urlMap.put("/ws", webSocketHandler);
//
//        Map<String, CorsConfiguration> corsConfigurationMap =
//                new HashMap<>();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
//                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
//        corsConfiguration.addAllowedOrigin("http://localhost:4200");
//        corsConfiguration.setAllowCredentials(true);
//        corsConfigurationMap.put("/ws", corsConfiguration);
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
//                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        corsConfiguration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
//        handlerMapping.setOrder(1);
//        handlerMapping.setUrlMap(urlMap);
//        return handlerMapping;
//    }
//
//    @Bean
//    public WebSocketHandlerAdapter handlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }
//}
