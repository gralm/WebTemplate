package se.hillerstadhill.backend.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished");
        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed");
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("handleTextMessage: " + message);
        System.out.println("handleTextMessage: " + message.getPayload());
        super.handleTextMessage(session, message);
        sessions.forEach(webSocketSession -> {
            try {
                System.out.println("Sending " + message + " back to " + webSocketSession);
                webSocketSession.sendMessage(new TextMessage("Get this stuff back"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
