package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {

    final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        sessions.add(session);
        log.info("session: " + sessionToString(session));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed");
        log.info("session: " + sessionToString(session));
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage: " + message);
        log.info("handleTextMessage: " + message.getPayload());
        log.info("session: " + sessionToString(session));
        super.handleTextMessage(session, message);
        sessions.forEach(webSocketSession -> {
            try {
                log.info("Sending " + message + " back to " + webSocketSession);
                webSocketSession.sendMessage(new TextMessage("Get this stuff back"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String sessionToString(WebSocketSession session) {
        return session == null ? "null" : session.getId() + ", " + session.getLocalAddress() + ", "
                + session.getRemoteAddress() + ", " + session.getUri();
    }
}
