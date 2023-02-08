package com.slinkdigital.feed.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.feed.dto.Message;
import com.slinkdigital.feed.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 *
 * @author TEGA
 */
@Component()
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private static final ObjectMapper json = new ObjectMapper();

    @Autowired()
    private KafkaService kafkaService;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(kafkaService.getTestTopicFlux()
                .map(record -> {
                    Message message = new Message("[Test] Add message", record.value());

                    try {
                        return json.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        return "Error while serializing to JSON";
                    }
                })
                .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText).log());
    }
}
