package ru.job4j.pooh.service;

import ru.job4j.pooh.Answers;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> store =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String type = req.getHttpRequestType();
        String source = req.getSourceName();
        String param = req.getParam();
        Resp result = new Resp("", Answers.NOT_IMPLEMENTED_METHOD.getValue());
        if (Answers.GET.getValue().equals(type)) {
            String status = Answers.GOOD.getValue();
            String text;
            store.putIfAbsent(source, new ConcurrentHashMap<>());
            store.get(source).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            String answer =
                    store.get(source).get(param).poll();
            text = answer;
            if (answer == null) {
                status = Answers.DATA_IS_NOT.getValue();
                text = Answers.EMPTY_ANSWER.getValue();
            }
            result = new Resp(text, status);
        } else if (Answers.POST.getValue().equals(type)) {
            String status;
            if (store.containsKey(source)) {
                store.get(source).forEach((key, value) -> value.add(param));
                status = Answers.GOOD.getValue();
            } else {
                status = Answers.DATA_IS_NOT.getValue();
            }
            result = new Resp(Answers.EMPTY_ANSWER.getValue(), status);
        }
        return result;
    }
}
