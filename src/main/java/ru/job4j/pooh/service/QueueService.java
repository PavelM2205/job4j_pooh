package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> store =
            new ConcurrentHashMap<>();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String GOOD = "200";
    private static final String DATA_IS_NOT = "204";
    private static final String EMPTY_ANSWER = "";


    @Override
    public Resp process(Req req) {
        String type = req.getHttpRequestType();
        String source = req.getSourceName();
        Resp result = null;
        if (GET.equals(type)) {
            String value = store.getOrDefault(source, new ConcurrentLinkedQueue<>()).poll();
            String status = GOOD;
            String text = value;
            if (value == null) {
                status = DATA_IS_NOT;
                text = EMPTY_ANSWER;
            }
        result = new Resp(text, status);
        } else if (POST.equals(type)) {
            String param = req.getParam();
            store.putIfAbsent(source, new ConcurrentLinkedQueue<>());
            store.get(source).add(param);
            result = new Resp(EMPTY_ANSWER, GOOD);
        }
        return result;
    }
}
