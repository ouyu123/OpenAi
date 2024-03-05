package com.example.chat_sdk_java.executor.result;

import okhttp3.sse.EventSourceListener;

public interface ResultHandler {
    EventSourceListener eventSourceListener(EventSourceListener eventSourceListener);
}
