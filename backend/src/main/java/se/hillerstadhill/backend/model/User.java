package se.hillerstadhill.backend.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Setter
public class User {
    private static int idCounter = 0;
    private int id;
    @NonNull
    private UUID uuid;

    // TODO: Should be socketSession
    private WebSocketSession socketSession;
    private String remoteIp;
    private LocalDateTime creationTime;
    private LocalDateTime lastAppearance;

    public User(UUID uuid, WebSocketSession socketSession, String remoteIp) {
        this.id = idCounter++;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.socketSession = socketSession;
        this.remoteIp = remoteIp;
        update();
        this.creationTime = lastAppearance;
    }

    public User update() {
        lastAppearance = LocalDateTime.now();
        return this;
    }

    public boolean equals(Object user) {
        return uuid != null && (user instanceof User) && this.uuid.equals(((User) user).uuid);
    }
}