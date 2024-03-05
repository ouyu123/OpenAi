package com.example.chat_sdk_java.executor;



import com.example.chat_sdk_java.domain.chat.SynRes.ChatCompletionSyncResponse;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.ImageCompletionRequest;
import com.example.chat_sdk_java.domain.chat.res.ImageCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Executor {

    /**
     * 问答模式，流式反馈
     *
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return 应答结果
     * @throws Exception 异常
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 问答模式，同步反馈 —— 用流式转化 Future
     *
     * @param chatCompletionRequest 请求信息
     * @return 应答结果
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    /**
     * 同步应答接口
     *
     * @param chatCompletionRequest 请求信息
     * @return ChatCompletionSyncResponse
     * @throws IOException 异常
     */
    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception;

    /**
     * 图片生成接口
     *
     * @param request 请求信息
     * @return 应答结果
     */
    ImageCompletionResponse genImages(ImageCompletionRequest request) throws Exception;

}
