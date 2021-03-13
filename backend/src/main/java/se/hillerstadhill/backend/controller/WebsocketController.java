package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/*
Trying example from
https://www.javainuse.com/spring/boot-websocket
Using Websockets without stomp
 */

@Controller
@Slf4j
public class WebsocketController {
    @MessageMapping("/hello")
    public String greeting(String message) throws InterruptedException {
        Thread.sleep(500);
        log.info("Received: " + message);
        return "Greeting";
    }
}
