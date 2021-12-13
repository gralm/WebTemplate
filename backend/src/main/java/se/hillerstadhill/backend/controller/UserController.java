package se.hillerstadhill.backend.controller;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

// TODO: Make this class control all active users by cookies, IP or sessionID
@Component
@Slf4j
public class UserController {
    public static final String COOKIE_UUID_NAME = "uuid_name";

    private ArrayList<User> users;
    // private ArrayList<User> bannedUsers;

    public UserController() {
        users = new ArrayList<>();
    }

    public User getUser(HttpServletRequest servletRequest, String remoteIP) {
        if (servletRequest == null) {
            return null;
        }

        User sessionUser = getUserByRequestCookie(servletRequest);
        if (sessionUser == null) {
            for (Cookie cookie : servletRequest.getCookies()) {
                if (cookie.getName().equals(COOKIE_UUID_NAME)) {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(cookie.getValue());
                    } catch (IllegalArgumentException iae) {
                        log.error("cookie.getValue() = " + cookie.getValue());
                        iae.printStackTrace();
                        return null;
                    }
                    User newUser = new User(uuid, null, remoteIP);
                    users.add(newUser);
                }
            }
        }
        return sessionUser;
    }

    public User getUser(WebSocketSession socketSession, String remoteIP) {
        if (socketSession == null) {
            return null;
        }

        User sessionUser = getUserBySession(socketSession);
        if (sessionUser == null) {
            if (socketSession.getId() == null) {
                // This should not occure
                throw new RuntimeException("getUser(socketSession), but socketSession.getId() = null");
            } else {
                User newUser = new User(null, socketSession.getId(), remoteIP);
                users.add(newUser);
                return newUser;
            }
        }
        return sessionUser;
    }

    public User getUserBySession(WebSocketSession socketSession) {
        if (socketSession == null) {
            return null;
        }
        Optional<User> oUser = this.users.stream()
                .filter(user -> socketSession.getId() == user.socketSessionId)
                .findFirst();
        return oUser.isPresent() ? oUser.get() : null;
    }

    public User getUserByIp(String remoteIP) {
        if (remoteIP == null) {
            return null;
        }
        Optional<User> oUser = this.users.stream()
                .filter(user -> remoteIP == user.remoteIp)
                .findFirst();
        return oUser.isPresent() ? oUser.get() : null;
    }

    public User getUserByRequestCookie(HttpServletRequest servletRequest) {
        if (servletRequest == null) {
            return null;
        }

        for (Cookie cookie : servletRequest.getCookies()) {
            if (cookie.getName().equals(COOKIE_UUID_NAME)) {
                Optional<User> oUser = this.users.stream().filter(user -> cookie.getValue().equals(user.uuid)).findFirst();
                return oUser.isPresent() ? oUser.get() : null;
            }
        }
        return null;
    }

    @ToString
    @Getter
    static class User {
        private UUID uuid;
        private String socketSessionId;
        private String remoteIp;
        private LocalDateTime creationTime;
        private LocalDateTime lastAppearance;

        public User(UUID uuid, String socketSessionId, String remoteIp) {
            this.uuid = uuid == null ? UUID.randomUUID() : uuid;
            this.socketSessionId = socketSessionId;
            this.remoteIp = remoteIp;
            update();
            this.creationTime = lastAppearance;
        }

        public User update() {
            lastAppearance = LocalDateTime.now();
            return this;
        }
    }
}
