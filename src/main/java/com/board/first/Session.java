package com.board.first;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Object> attributes;

    public Session() {
        // 각각의 세션이 독립적인 데이터를 갖도록 하기 위함.
        this.attributes = new HashMap<>();
    }

    public void setAttributes(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
}
