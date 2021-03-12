package se.hillerstadhill.backend.controller.eventhandler;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MyEventHandlerTest {
    private MyEventHandler sut;
    private List<LocalEvent> events;
    private long now;
    private EventAction eventAction;

    @BeforeEach
    void setUp() {
        sut = new MyEventHandler();
        events = new ArrayList<>();
        now = System.currentTimeMillis();
        eventAction = refId -> {
            events.add(new LocalEvent(System.currentTimeMillis(), refId));
            //System.out.println("Occurence: " + (System.currentTimeMillis()-now)  + ", " + refId);
        };
    }

    @Test
    void addEvent0_5() {
        createEvent(300, 1);
        createEvent(200, 1);
        sleep(400);
        assertOccurred(200, 1);
    }


    @Test
    void addEvent() {
        createEvent(300, 1);
        createEvent(150, 2);
        sleep(500);
        assertOccurred(300, 1);
        assertOccurred(150, 2);
        Assertions.assertEquals(2, events.size());
    }

    @Test
    void addEvent2() {
        createEvent(300, 1);
        createEvent(150, 2);
        createEvent(250, 2);
        createEvent(150, 1);
        sleep(500);
        assertOccurred(150, 1);
        assertOccurred(250, 2);
        Assertions.assertEquals(2, events.size());
    }

    @Test
    void addEvent3() {
        createEvent(300, 1);
        createEvent(150, 2);
        createEvent(250, 3);
        sleep(500);
        assertOccurred(300, 1);
        assertOccurred(150, 2);
        assertOccurred(250, 3);
    }


    @Test
    void addEvent4() {
        createEvent(300, 1);
        createEvent(150, 2);
        createEvent(250, 3);
        createEvent(450, 4);
        sleep(350);
        Assertions.assertEquals(3, events.size());
        sleep(150);
        Assertions.assertEquals(4, events.size());
    }


    @Test
    void addEvent5() {
        createEvent(300, 1);
        createEvent(150, 2);
        createEvent(250, 3);
        createEvent(450, 4);
        sleep(350);
        Assertions.assertEquals(3, events.size());

        createEvent(400, 5);
        createEvent(500, 4);
        sleep(300);

        assertOccurred(400, 5);
        assertOccurred(500, 4);
        assertOccurredNumOfTimes(450, 4, 0);
        Assertions.assertEquals(5, events.size());
    }



    private void assertOccurredNumOfTimes(long time, int refId, int numOfTimes) {
        assertOccurred(time, 25, refId, numOfTimes);
    }

    private void assertOccurred(long time, int refId) {
        assertOccurred(time, 25, refId, 1);
    }

    private void createEvent(long relativeTime, int refId) {
        sut.addEvent(now + relativeTime, refId, eventAction);
    }

    private void assertOccurred(long relativeTime, long dt, int refId, int expectedNumOfTimes) {
        final long time = relativeTime + now;
        long numOfEvents = events.stream().filter(localEvent ->
                (localEvent.refId == refId) && (localEvent.time > time - dt) && (localEvent.time < time + dt)
        ).count();
        Assertions.assertEquals(expectedNumOfTimes, numOfEvents);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addEventNPEWithoutAction() {
        assertThrows(NullPointerException.class, () -> sut.addEvent(100, 0, null));
    }

    @ToString
    @AllArgsConstructor
    private static class LocalEvent {
        long time;
        int refId;
    }
}