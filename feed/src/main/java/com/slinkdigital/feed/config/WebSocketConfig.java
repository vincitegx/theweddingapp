package com.slinkdigital.feed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ui client will use this to connect to the server
        registry.addEndpoint("/ws-notification").setAllowedOrigins("http://localhost:4200").withSockJS();
    }
    
    @Override
    public void configureWebSocketTransport( WebSocketTransportRegistration registration )
    {
        registration.setMessageSizeLimit( 300000 * 50 );
        registration.setSendTimeLimit( 30 * 10000 );
        registration.setSendBufferSizeLimit( 3 * 512 * 1024 );
    }
}
