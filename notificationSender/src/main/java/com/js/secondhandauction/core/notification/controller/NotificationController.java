package com.js.secondhandauction.core.notification.controller;

import com.js.secondhandauction.websocket.ReactiveWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Sinks;

@RestController
@Slf4j
public class NotificationController {

    @Autowired
    private ReactiveWebSocketHandler webSocketHandler;

    //Test Send a message to a specific user
    @PostMapping("/message/{userno}")
    public ResponseEntity<String> postMessage(@PathVariable String userno, @RequestBody String message) {
        Sinks.Many<String> sink = webSocketHandler.getSink(userno);
        if (sink != null) {
            sink.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
            return ResponseEntity.ok("Message Sent!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        }
    }

}