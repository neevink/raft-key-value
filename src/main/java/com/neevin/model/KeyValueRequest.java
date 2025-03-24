package com.neevin.model;

import java.util.Arrays;

public class KeyValueRequest {
    private String key;
    private String value;

    // Стандартные геттеры и сеттеры
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValueRequest{" +
                "key='" + key + '\'' +
                ", value='" + (value != null ? "[binary data]" : "null") + '\'' +
                '}';
    }
}

