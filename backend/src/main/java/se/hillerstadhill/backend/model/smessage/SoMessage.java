package se.hillerstadhill.backend.model.smessage;

import lombok.*;

/**
 * Abstract Socket-Output-Message
 * message from backend to frontend
 */
@NoArgsConstructor
@ToString
public class SoMessage {
    private String type;
    @Setter
    @Getter
    private String payload;

    public void setType(SoType type) {
        this.type = type.name();
    }

    public SoType getType() {
        return this.type == null ? null: SoType.valueOf(this.type);
    }
}
