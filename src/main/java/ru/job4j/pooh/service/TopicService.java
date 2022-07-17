package ru.job4j.pooh.service;

import ru.job4j.pooh.store.CASMapInMap;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

public class TopicService implements Service {
    private final CASMapInMap store = new CASMapInMap();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String GOOD = "200";
    private static final String DATA_IS_NOT = "204";
    private static final String EMPTY_ANSWER = "";

    @Override
    public Resp process(Req req) {
        String type = req.getHttpRequestType();
        String source = req.getSourceName();
        String param = req.getParam();
        Resp result = null;
        if (GET.equals(type)) {
            String status = GOOD;
            String text;
            store.addTopic(source);
            store.addIndividualQueue(source, req.getParam());
            String answer = store.getFromIndividualQueue(source, param);
            text = answer;
            if (answer == null) {
                status = DATA_IS_NOT;
                text = EMPTY_ANSWER;
            }
            result = new Resp(text, status);
        } else if (POST.equals(type)) {
            String status;
            if (store.isTopic(source)) {
                store.addIntoEachIndividualQueue(source, param);
                status = GOOD;
            } else {
                status = DATA_IS_NOT;
            }
            result = new Resp(EMPTY_ANSWER, status);
        }
        return result;
    }
}
