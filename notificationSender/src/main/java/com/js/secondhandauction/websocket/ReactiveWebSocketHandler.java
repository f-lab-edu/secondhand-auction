package com.js.secondhandauction.websocket;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final Map<String, Sinks.Many<String>> sinkMap = new ConcurrentHashMap<>();


    @Override
    @NonNull
    public Mono<Void> handle(WebSocketSession session) {
        log.info("Session Opened");

        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(userNo -> {
                    sinkMap.computeIfAbsent(userNo, k -> {
                        Sinks.Many<String> newSink = Sinks.many().multicast().directBestEffort();
                        session.send(newSink.asFlux().map(session::textMessage)).subscribe();
                        return newSink;
                    });
                    return Mono.empty();
                });
        return Mono.empty();
    }

    public Sinks.Many<String> getSink(String userNo) {
        return sinkMap.get(userNo);
    }
}
