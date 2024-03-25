package com.js.secondhandauction.websocket;

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
    public Mono<Void> handle(WebSocketSession session) {
        log.info("Session Opened");

        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(userno -> {
                    Sinks.Many<String> sink = sinkMap.computeIfAbsent(userno, k -> Sinks.many().multicast().directBestEffort());
                    session.send(sink.asFlux().map(session::textMessage)).subscribe();
                    return Mono.empty();
                })
                .then();
    }

    public Sinks.Many<String> getSink(String userNo) {
        return sinkMap.get(userNo);
    }
}
