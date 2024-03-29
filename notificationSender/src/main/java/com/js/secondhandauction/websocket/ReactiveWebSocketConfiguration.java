package com.js.secondhandauction.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class ReactiveWebSocketConfiguration {
    @Bean
    public SimpleUrlHandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping(Map.of("/sender/notice-emitter", wsh), 1);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
