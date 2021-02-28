package se.hillerstadhill.backend.controller;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*
        setAllowedOrigins("*") låter websockets fungera i react/typescript-exemplet
        men det fungerar utan i javascript-exemplet
         */
        registry.addHandler(new SocketTextHandler(), "/user").setAllowedOrigins("*");
    }
}
