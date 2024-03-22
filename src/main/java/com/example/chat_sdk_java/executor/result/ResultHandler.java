package com.example.chat_sdk_java.executor.result;

import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.res.ChatRoleCompletionResponse;
import okhttp3.sse.EventSourceListener;

public interface ResultHandler {
    EventSourceListener eventSourceListener(EventSourceListener eventSourceListener);

}
