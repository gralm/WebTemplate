package se.hillerstadhill.backend.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import se.hillerstadhill.backend.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

// TODO: Make this class control all active users by cookies, IP or sessionID
@Component
@Slf4j
public class UserController {
    public static final String COOKIE_UUID_NAME = "uuid_name";

    private LinkedList<User> users;
    // private ArrayList<User> bannedUsers;

    public UserController() {
        users = new LinkedList<>();
    }

    public User getUser(WebSocketSession socketSession) {
        if (socketSession == null) {
            return null;
        }

        User user = getUserBySession(socketSession);
        if (user != null) {
            return user;
        }

        // TODO: Do this in a better way
        String remoteIP = socketSession.getRemoteAddress().getHostString();
        log.info("remoteIP: " + remoteIP);
        user = getUserByIp(remoteIP);

        if (user != null) {
            user.setSocketSession(socketSession);
        }
        return user;
    }

    public User getUser(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (User user: users) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public User turnOffSocketConnection(WebSocketSession socketSession) {
        if (socketSession == null) {
            return null;
        }
        ListIterator<User> userIter = users.listIterator();
        while (userIter.hasNext()) {
            User user = userIter.next();
            user.setSocketSession(null);
            return user;
        }
        return null;
    }

    public User getUserBySession(WebSocketSession socketSession, @NonNull UUID uuid) {
        User userSocket = getUserBySession(socketSession);
        User userUuid = getUser(uuid);
        if (userSocket == null) {
            if (userUuid != null) {
                userUuid.setSocketSession(socketSession);
            }
            return userUuid;
        } else if (userSocket.getUuid().equals(uuid)) {
            return userSocket;
        } else {
            log.error("error in getUserBySession(WebSocketSession socketSession, @NonNull UUID uuid)"
                    + "socketSession(" + socketSession + ") matches " + userSocket + " but its uuid is no equal to = "
                    + uuid);
        }
        return null;
    }

    public User getUserBySession(WebSocketSession socketSession) {
        if (socketSession == null) {
            return null;
        }
        Optional<User> oUser = this.users.stream()
                .filter(user -> user.getSocketSession() != null && socketSession.getId().equals(user.getSocketSession().getId()))
                .findFirst();
        return oUser.isPresent() ? oUser.get() : null;
    }

    public User getUserByIp(String remoteIP) {
        if (remoteIP == null) {
            return null;
        }
        for (User user: users) {
            if (user.getRemoteIp().equals(remoteIP)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByRequestCookie(HttpServletRequest servletRequest) {
        if (servletRequest == null || servletRequest.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : servletRequest.getCookies()) {
            if (cookie.getName().equals(COOKIE_UUID_NAME)) {
                Optional<User> oUser = this.users.stream().filter(user -> cookie.getValue().equals(user.getUuid().toString())).findFirst();
                return oUser.isPresent() ? oUser.get() : null;
            }
        }
        return null;
    }

    public void createUser(User user) {
        users.add(user);
    }

    private String getIpAdderss(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return ipAddress == null ? request.getRemoteAddr() : ipAddress;
    }
}
