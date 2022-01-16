package se.hillerstadhill.backend.model.smessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SMessageFactory {
    private ObjectMapper objectMapper;

    public SiMessage parseToSiMessage(String payload) {
        SiMessage siMessage;
        try {
            System.out.println("payload: " + payload);
            siMessage = objectMapper.readValue(payload, SiMessage.class);
        } catch (JsonMappingException e) {
            log.error("Could not read socket message: " + payload, e);
            return null;
        } catch (JsonProcessingException e) {
            log.error("Could not read socket message: " + payload, e);
            return null;
        }

        return siMessage.getType() == null ? null : siMessage;
    }

    public String parseToString(SoMessage siMessage) {
        try {
            return objectMapper.writeValueAsString(siMessage);
        } catch (JsonProcessingException e) {
            log.error("Could not convert siMessage to String: " + siMessage, e);
        }
        return null;
    }
}