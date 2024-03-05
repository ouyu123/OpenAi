package com.example.chat_sdk_java.session.defaults;

import com.example.chat_sdk_java.domain.chat.SynRes.ChatCompletionSyncResponse;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.ImageCompletionRequest;
import com.example.chat_sdk_java.domain.chat.res.ImageCompletionResponse;
import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.example.chat_sdk_java.executor.Executor;
import com.example.chat_sdk_java.session.Configuration;
import com.example.chat_sdk_java.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @description 会话服务
 * @github https://github.com/fuzhengwei/chatglm-sdk-java/chatglm-sdk-java
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
@Slf4j
public class DefaultOpenAiSession implements OpenAiSession {

    private final Configuration configuration;
    private final Map<Model, Executor> executorGroup;

    public DefaultOpenAiSession(Configuration configuration, Map<Model, Executor> executorGroup) {
        this.configuration = configuration;
        this.executorGroup = executorGroup;
    }
    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completions(chatCompletionRequest, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completions(chatCompletionRequest);
    }

    @Override
    public ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completionsSync(chatCompletionRequest);
    }

    @Override
    public ImageCompletionResponse genImages(ImageCompletionRequest imageCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(imageCompletionRequest.getModelEnum());
        if (null == executor) throw new RuntimeException(imageCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.genImages(imageCompletionRequest);
    }

    @Override
    public Configuration configuration() {
        return configuration;
    }

}
