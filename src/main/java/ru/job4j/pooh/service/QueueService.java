package ru.job4j.pooh.service;

import ru.job4j.pooh.Answers;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> store =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String type = req.getHttpRequestType();
        String source = req.getSourceName();
        Resp result = new Resp("", Answers.NOT_IMPLEMENTED_METHOD.getValue());
        if (Answers.GET.getValue().equals(type)) {
            String value = store.getOrDefault(source, new ConcurrentLinkedQueue<>()).poll();
            String status = Answers.GOOD.getValue();
            String text = value;
            if (value == null) {
                status = Answers.DATA_IS_NOT.getValue();
                text = Answers.EMPTY_ANSWER.getValue();
            }
        result = new Resp(text, status);
        } else if (Answers.POST.getValue().equals(type)) {
            String param = req.getParam();
            store.putIfAbsent(source, new ConcurrentLinkedQueue<>());
            store.get(source).add(param);
            result = new Resp(Answers.EMPTY_ANSWER.getValue(), Answers.GOOD.getValue());
        }
        return result;
    }
}
