package se.hillerstadhill.backend.controller.eventhandler;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MyEventHandler implements EventHandler {
    private TreeMap<Long, Event> treeMap;
    private Timer timer;
    private Lock lock;

    public MyEventHandler() {
        this.treeMap = new TreeMap<>((t1, t2) -> t1 < t2 ? -1 : 1);
        lock = new ReentrantLock();
        timer = new Timer();
    }

    public void addEvent(long timeMs, int refId, @NonNull EventAction eventAction) {
        addEvent(new Event(timeMs, refId, eventAction));
    }

    public void printTreemap(String mess) {
        if (lock.tryLock()) {
            for (Map.Entry<Long, Event> asdf : treeMap.entrySet()) {
                System.out.println(mess + ": " + asdf.getKey() + ", " + asdf.getValue().refId);
            }
            lock.unlock();
        } else {
            System.out.println("Gick inte att locka för utskrift");
        }
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private void addEvent(Event event) {
        lock.lock();
        if (this.treeMap.isEmpty()) {
            if (event.timeMs <= now()) {
                takeActionImmediately(event);
            } else {
                treeMap.put(event.timeMs, event);
                schedule(event.timeMs);
            }
        } else {
            removeRefIdAndReschedule(event.refId);
            if (this.treeMap.isEmpty()) {
                if (event.timeMs <= now()) {
                    takeActionImmediately(event);
                } else {
                    treeMap.put(event.timeMs, event);
                    schedule(event.timeMs);
                }
            } else if (event.timeMs < this.treeMap.firstKey()) {
                treeMap.put(event.timeMs, event);
                schedule(event.timeMs);
            } else {
                treeMap.put(event.timeMs, event);
            }
        }
        lock.unlock();
    }

    private void schedule(long time) {
        timer.cancel();
        timer = new Timer();
        Date date = new Date(time);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lock.lock();
                Map.Entry<Long, Event> entry = treeMap.pollFirstEntry();
                takeActionImmediately(entry.getValue());
                if (!treeMap.isEmpty()) {
                    schedule(treeMap.firstKey());
                }
                lock.unlock();
            }
        }, date);
    }

    private void removeRefIdAndReschedule(int refId) {
        lock.lock();
        Iterator<Map.Entry<Long, Event>> entryIterator = this.treeMap.entrySet().iterator();

        if (!entryIterator.hasNext()) {
            lock.unlock();
            return;
        }

        Map.Entry<Long, Event> entry = entryIterator.next();
        // Om refId ligger först
        if (entry.getValue().refId == refId) {
            entryIterator.remove();
            if (!entryIterator.hasNext()) {
                lock.unlock();
                return;
            }
            schedule(entryIterator.next().getKey());
            lock.unlock();
            return;
        }

        // Om man hittar refId senare
        while (entryIterator.hasNext()) {
            entry = entryIterator.next();
            if (entry.getValue().refId == refId) {
                entryIterator.remove();
                lock.unlock();
                return;
            }
        }
        lock.unlock();
    }


    private void takeActionImmediately(Event event) {
        event.eventAction.run(event.refId);
    }

    @AllArgsConstructor
    @ToString
    private static class Event {
        long timeMs;
        int refId;
        EventAction eventAction;
    }
}

