package se.hillerstadhill.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.web.socket.WebSocketSession;
import se.hillerstadhill.backend.model.User;

import java.net.InetSocketAddress;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private User users[];

    @BeforeEach
    public void setup() {
        userController = new UserController();
        users = new User[10];
        for (int i = 0; i < users.length; i++) {
            boolean activeSession = (i % 2) == 0;
            users[i] = createUser(activeSession);
            userController.createUser(users[i]);
        }
    }

    @Test
    public void testGetUser() {
        for (int i = 0; i < users.length; i++) {
            User user = userController.getUser(users[i].getUuid());
            assertThat(user.getRemoteIp(), is(users[i].getRemoteIp()));
        }
    }

    @Test
    public void testGetUserBySession() {
        final int oddUserId = 5;
        WebSocketSession presentSesssion = createWebSocketSession(users[oddUserId]);
        users[oddUserId].setSocketSession(presentSesssion);
        User user = userController.getUserBySession(presentSesssion);
        assertThat(user.getUuid(), is(users[oddUserId].getUuid()));
    }

    @Test
    public void testTurnOffSocketConnection() {
        int userId = 2;
        assertThat(users[userId].getSocketSession() != null, is(true));
        User user = userController.turnOffSocketConnection(users[userId].getSocketSession());
        assertThat(users[userId].getSocketSession() == null, is(true));
        assertThat(user.getUuid(), is(users[userId].getUuid()));
    }

    @Test
    public void testTurnOnSocketConnection() {
        int userId = 3;
        assertThat(users[userId].getSocketSession() == null, is(true));
        User user = userController.turnOnSocketConnection(users[userId].getSocketSession());
        assertThat(users[userId].getSocketSession() != null, is(true));
        assertThat(user.getUuid(), is(users[userId].getUuid()));

    }

    private User createUser(boolean activeSocketSession) {
        String randomIp = "";
        for (int i = 0; i < 4; i++) {
            int num = (int) (Math.random() * 256);
            randomIp += (i == 0 ? "" : ".") + num;
        }
        String randomId = new AlternativeJdkIdGenerator().generateId().toString();
        User user = new User(UUID.randomUUID(), activeSocketSession ? createWebSocketSession(randomIp, randomId) : null, randomIp);
        return user;
    }

    private WebSocketSession createWebSocketSession(User user) {
        if (user.getSocketSession() == null) {
            return createWebSocketSession(user.getRemoteIp(), new AlternativeJdkIdGenerator().generateId().toString());
        }
        return createWebSocketSession(user.getRemoteIp(), user.getSocketSession().getId());
    }

    private WebSocketSession createWebSocketSession(String ip, String id) {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getRemoteAddress()).thenReturn(new InetSocketAddress(ip, 8080));
        when(session.getId()).thenReturn(id);
        return session;
    }
}