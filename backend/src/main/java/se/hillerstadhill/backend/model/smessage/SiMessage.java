package se.hillerstadhill.backend.model.smessage;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Abstract Socket-Input-Message
 * message from frontend to backend
 */
@Data
@NoArgsConstructor
@ToString
@Slf4j
public class SiMessage {
    private String type;
    private String uuid;
    private String payload;

    public UUID getUUID() {
        try {
            return uuid == null ? null : UUID.fromString(uuid);
        } catch(IllegalArgumentException e) {
            log.error("getUUID() misslyckades med uuid=\"" + uuid + "\": " + e);
        }
        return null;
    }
}
