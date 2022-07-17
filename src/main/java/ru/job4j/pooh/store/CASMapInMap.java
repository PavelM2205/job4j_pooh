package ru.job4j.pooh.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CASMapInMap {
    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queue =
            new ConcurrentHashMap<>();

    public void addTopic(String topicName) {
        queue.putIfAbsent(topicName, new ConcurrentHashMap<>());
    }

    public void addIndividualQueue(String topicName, String individualQueueName) {
        queue.get(topicName).putIfAbsent(individualQueueName, new ConcurrentLinkedQueue<>());
    }

    public String getFromIndividualQueue(String topicName, String individualQueueName) {
        return queue.get(topicName).get(individualQueueName).poll();
    }

    public void addIntoEachIndividualQueue(String topicName, String valueForIndividualQueue) {
        queue.get(topicName).forEach((key, value) -> value.add(valueForIndividualQueue));
    }

    public boolean isTopic(String topicName) {
        return queue.containsKey(topicName);
    }
}
