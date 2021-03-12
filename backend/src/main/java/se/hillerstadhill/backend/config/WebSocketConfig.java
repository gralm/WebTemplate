package se.hillerstadhill.backend.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import se.hillerstadhill.backend.controller.SocketTextHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private SocketTextHandler socketTextHandler;
    public WebSocketConfig(SocketTextHandler socketTextHandler) {
        this.socketTextHandler = socketTextHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*
        setAllowedOrigins("*") låter websockets fungera i react/typescript-exemplet
        men det fungerar utan i javascript-exemplet
         */
        System.out.println(socketTextHandler);
        registry.addHandler(socketTextHandler, "/user").setAllowedOrigins("*");
    }
}
