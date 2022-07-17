package ru.job4j.pooh;

import org.junit.jupiter.api.Test;
import ru.job4j.pooh.service.QueueService;

import static org.assertj.core.api.Assertions.*;

class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getText()).isEqualTo("temperature=18");
    }

    @Test
    public void whenEmptyQueueThenGetReturnEmptyAnswer() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.getText()).isEqualTo("");
        assertThat(result.getStatus()).isEqualTo("204");
    }

    @Test
    public void whenOneTimePostAndTwoTimesGetThenSecondResponseMustBeEmpty() {
        QueueService queueService = new QueueService();
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result1.getStatus()).isEqualTo("200");
        assertThat(result2.getText()).isEqualTo("");
        assertThat(result2.getStatus()).isEqualTo("204");
    }
}