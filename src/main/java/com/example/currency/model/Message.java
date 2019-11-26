package com.example.currency.model;

import java.util.Optional;

public class Message {

    private String key;
    private Object content;

    public Message() {}

    public Message(String key) {
        this.key = key;
    }

    public Message(String key, Object content) {
        this.key = key;
        this.content = content;
    }

    public Optional<Object> getContent() {
        return Optional.ofNullable(content);
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Optional<String> getKey() {
        return Optional.ofNullable(key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
        .append("message: {\nkey" + getKey().orElse("") + ",\ncontent: " + getContent().orElse("{}") + "\n}");
        return super.toString();
    }
}
