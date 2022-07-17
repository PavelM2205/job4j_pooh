package ru.job4j.pooh.service;

import ru.job4j.pooh.store.CASMap;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

public class QueueService implements Service {
    private final CASMap store = new CASMap();
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
            String value = store.getValue(source);
            String status = GOOD;
            String text = value;
            if (value == null) {
                status = DATA_IS_NOT;
                text = EMPTY_ANSWER;
            }
        result = new Resp(text, status);
        } else if (POST.equals(type)) {
            store.addQueue(source);
            store.putValue(source, req.getParam());
            result = new Resp(EMPTY_ANSWER, GOOD);
        }
        return result;
    }
}
