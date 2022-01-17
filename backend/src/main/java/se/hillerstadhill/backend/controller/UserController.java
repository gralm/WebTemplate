package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import se.hillerstadhill.backend.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

// TODO: Make this class control all active users by cookies, IP or sessionID
@Component
@Slf4j
public class UserController {
    public static final String COOKIE_UUID_NAME = "uuid_name";
    private static final long CLEAN_USERS_PERIOD = 1000 * 3600;

    // TODO: change to Map<UUID, User> users
    private Map<UUID, User> users;
    // private ArrayList<User> bannedUsers;

    public UserController() {
        users = new HashMap<>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeInactiveUsers();
            }
        }, CLEAN_USERS_PERIOD, CLEAN_USERS_PERIOD);
    }

    public User getUserBySession(WebSocketSession socketSession) {
        if (socketSession == null || socketSession.getId() == null) {
            return null;
        }
        Optional<User> oUser;
        synchronized (users) {
            oUser = this.users.entrySet().stream()
                    .map(entry -> entry.getValue())
                    .filter(entry -> entry.getSocketSession() != null && entry.getSocketSession().getId() != null
                            && entry.getSocketSession().getId().equals(socketSession.getId()))

                    .findFirst();
        }
        return oUser.isPresent() ? oUser.get() : null;
    }

    public User getUser(UUID uuid) {
        synchronized (users) {
            return this.users.get(uuid);
        }
    }

    public User turnOffSocketConnection(WebSocketSession socketSession) {
        if (socketSession == null) {
            log.error("turnOffSocketConnection(null);");
            return null;
        }
        User user = getUserBySession(socketSession);
        if (user == null) {
            return null;
        }
        user.setSocketSession(null);
        return user;
    }

    public User turnOnSocketConnection(WebSocketSession socketSession) {
        if (socketSession != null) {
            log.error("turnOnSocketConnection(not null); " + socketSession);
            return null;
        }
        User user = getUserBySession(socketSession);
        if (user == null) {
            log.error("turnOnSocketConnection for non existing user");
            return null;
        }
        user.setSocketSession(socketSession);
        return user;
    }

    public User getUserByIp(String remoteIP) {
        if (remoteIP == null) {
            return null;
        }
        Optional<User> uuid;

        synchronized (users) {
            uuid = this.users.entrySet().stream()
                    .filter(entry -> entry.getValue().getRemoteIp().equals(remoteIP))
                    .map(entry -> entry.getValue()).findFirst();
        }

        return uuid.isPresent() ? uuid.get() : null;
    }

    public User getUserByRequestCookie(HttpServletRequest servletRequest) {
        if (servletRequest == null || servletRequest.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : servletRequest.getCookies()) {
            if (cookie.getName().equals(COOKIE_UUID_NAME)) {
                try {
                    UUID uuid = UUID.fromString(cookie.getValue());
                    return getUser(uuid);
                } catch (IllegalArgumentException e) {
                    log.error("UUID.fromString(" + cookie.getValue() + ") failed, ", e);
                }
                return null;
            }
        }
        return null;
    }

    public void createUser(User user) {
        synchronized (users) {
            users.put(user.getUuid(), user);
        }
    }

    private String getIpAdderss(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return ipAddress == null ? request.getRemoteAddr() : ipAddress;
    }

    private void removeInactiveUsers() {
        long timeNow = System.currentTimeMillis();
        HashMap<UUID, User> newUserMap = new HashMap<>();

        synchronized (users) {
            this.users.entrySet().stream()
                    .filter(entry -> entry.getValue().getLastAppearance() < timeNow - CLEAN_USERS_PERIOD)
                    .forEach(entry -> newUserMap.put(entry.getKey(), entry.getValue()));
            this.users = newUserMap;
        }
    }
}
