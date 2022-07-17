package ru.job4j.pooh.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CASMap {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    public void addQueue(String name) {
        queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
    }

    public void putValue(String queueName, String value) {
        queue.get(queueName).add(value);
    }

    public String getValue(String queueName) {
        return queue.getOrDefault(queueName, new ConcurrentLinkedQueue<>()).poll();
    }
}
