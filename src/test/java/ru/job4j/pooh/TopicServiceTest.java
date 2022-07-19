package ru.job4j.pooh;

import org.junit.jupiter.api.Test;
import ru.job4j.pooh.service.TopicService;

import static org.assertj.core.api.Assertions.*;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result2.getText()).isEqualTo("");
    }

    @Test
    public void whenPostIntoEmptyQueueThenMustBeEmptyResponse() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        Resp result = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        assertThat(result.getText()).isEqualTo("");
        assertThat(result.getStatus()).isEqualTo("204");
    }

    @Test
    public void whenGetIntoEmptyQueueThenMustBeEmptyResponse() {
        TopicService topicService = new TopicService();
        String paramForSubscriber1 = "client407";
        Resp result = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(result.getText()).isEqualTo("");
        assertThat(result.getStatus()).isEqualTo("204");
    }

    @Test
    public void whenTwoGetAndOnePostThenMustBeAnswerForBothResponses() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        Req get1 = new Req("GET", "topic", "weather", paramForSubscriber1);
        Req get2 = new Req("GET", "topic", "weather", paramForSubscriber2);
        topicService.process(get1);
        topicService.process(get2);
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp resp1 = topicService.process(get1);
        Resp resp2 = topicService.process(get2);
        assertThat(resp1.getText()).isEqualTo("temperature=18");
        assertThat(resp1.getStatus()).isEqualTo("200");
        assertThat(resp2.getText()).isEqualTo("temperature=18");
        assertThat(resp2.getStatus()).isEqualTo("200");
    }
}