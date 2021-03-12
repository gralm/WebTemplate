package se.hillerstadhill.backend.controller.eventhandler;

import lombok.NonNull;

public interface EventHandler {
    void addEvent(long timeMs, int refId, @NonNull EventAction eventAction);
}
