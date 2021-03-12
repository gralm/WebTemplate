package se.hillerstadhill.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/*
Trying example from
https://www.javainuse.com/spring/boot-websocket
Using Websockets without stomp
 */

@Controller
public class WebsocketController {
    @MessageMapping("/hello")
    public String greeting(String message) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("Tog emot: " + message);
        return "Greeting";
    }
}
