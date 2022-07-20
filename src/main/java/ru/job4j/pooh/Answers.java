package ru.job4j.pooh;

public enum Answers {
    GET("GET"), POST("POST"), GOOD("200"), DATA_IS_NOT("204"),
    EMPTY_ANSWER(""), NOT_IMPLEMENTED_METHOD("501");

    private String value;

    Answers(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
