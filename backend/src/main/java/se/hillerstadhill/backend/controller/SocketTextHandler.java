package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import se.hillerstadhill.backend.model.User;
import se.hillerstadhill.backend.model.smessage.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static se.hillerstadhill.backend.model.smessage.SiType.CONNECT_USER;

@Component
@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {

    final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private UserController userController;
    private SMessageFactory sFactory;

    public SocketTextHandler(UserController userController, SMessageFactory sFactory) {
        this.userController = userController;
        this.sFactory = sFactory;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        User user = this.userController.getUserBySession(session);
        log.info("user: "+ user);
        if (user != null) {
            user.update();
        }
        sessions.add(session);
        log.info("session: " + sessionToString(session));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed (" + status + ")");
        log.info("session: " + sessionToString(session));
        User user = userController.getUserBySession(session);
        log.info("user: " + user);
        userController.turnOffSocketConnection(session);
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("handleTextMessage: " + message);
        log.info("handleTextMessage: " + message.getPayload());
        log.info("session: " + sessionToString(session));

        SiMessage siMessage = this.toSIMessage(message);
        if (siMessage == null) {
            return;
        }

        // if message contains UUID but uuid is not in memory
        User user = getUser(session, siMessage);
        if (user == null) {
            // this message should be CONNECT_USER
            if (!siMessage.getType().equals(CONNECT_USER.name())) {
                log.warn("siMessage should connect with CONNECT_USER but siMessage.type = " + siMessage.getType());
            }
            user = new User(siMessage.getUUID(), session, session.getRemoteAddress().getHostString());
            log.info("created new user " + user);
            SoMessage soMessage = new SoMessage();

            // Reply with new user-information to user.
            soMessage.setType(SoType.NEW_USER);
            TextMessage newUserTextMessage = toTextMessage(soMessage);
            log.info("newUserTextMessage: " + newUserTextMessage);
            session.sendMessage(newUserTextMessage);
            userController.createUser(user);
        }

        SiType siType = SiType.valueOf(siMessage.getType());
        switch (siType) {
            case MESSAGE:
                // handle messsage
                break;
            case CONNECT_USER:
                log.info("CONNECT_USER");
                break;
            case DISCONNECT_USER:
                log.info("DISCONNECT_USER");
                super.afterConnectionClosed(session, CloseStatus.NORMAL);
                break;
            default:
                log.warn("siType: " + siType);
        }

        log.info("using user: " + user);

    }

    public void sendMessage(UUID uuid, String message) {
        User user = userController.getUser(uuid);
        if (user == null || user.getSocketSession() == null) {
            return;
        }

        SoMessage soMessage = new SoMessage();
        soMessage.setType(SoType.MESSAGE);
        soMessage.setPayload(message);
        log.info("soMessage: " + soMessage);
        try {
            user.getSocketSession().sendMessage(toTextMessage(soMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SiMessage toSIMessage(TextMessage message) {
        if (message != null && message.getPayload() != null) {
            String payload = message.getPayload();
            return this.sFactory.parseToSiMessage(payload);
        }
        log.error("invalid sMessage: " + ((message == null) ? message : message.getPayload()));
        return null;
    }

    private TextMessage toTextMessage(SoMessage message) {
        if (message == null) {
            log.error("invalid AbstractSoMessage: " + message);
            return null;
        }

        String textMessage = this.sFactory.parseToString(message);
        log.info("send textmessage = " + textMessage);
        if (textMessage == null) {
            log.error("AbstractSoMessage not parseable: " + message);
            return null;
        }

        return new TextMessage(textMessage);
    }

    private String sessionToString(WebSocketSession session) {
        return session == null ? "null" : session.getId() + ", " + session.getLocalAddress() + ", "
                + session.getRemoteAddress() + ", " + session.getUri();
    }

    private User getUser(WebSocketSession session, SiMessage siMessage) {
        User userByUuid = this.userController.getUser(siMessage.getUUID());
        User userBySession = this.userController.getUserBySession(session);
        log.info("userByUuid: " + userByUuid);
        log.info("userBySession: "+ userBySession);
        if (userBySession == null) {
            if (userByUuid != null) {
                log.warn("found userByUuid = " + userByUuid.getId() + ", but no userBySession. Return userByUuid");
                return userByUuid;
            }
            return null;
        } else if (userByUuid == null) {
            log.info("found user by session");
            return userBySession;
        } else if (!userBySession.equals(userByUuid)) {
            log.warn("userByUuid = " + userByUuid + " != userBySession = " + userBySession);
            return userBySession;
        }
        return userBySession;
    }
}
