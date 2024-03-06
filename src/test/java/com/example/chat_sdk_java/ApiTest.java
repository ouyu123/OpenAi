package com.example.chat_sdk_java;



import com.alibaba.fastjson.JSON;
import com.example.chat_sdk_java.domain.chat.entity.Usage;
import com.example.chat_sdk_java.domain.chat.entity.WebSearch;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.ImageCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.Message;
import com.example.chat_sdk_java.domain.chat.req.Tool;
import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.vo.EventType;
import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.example.chat_sdk_java.domain.chat.vo.Role;
import com.example.chat_sdk_java.domain.chat.vo.ToolType;
import com.example.chat_sdk_java.session.Configuration;
import com.example.chat_sdk_java.session.OpenAiSession;
import com.example.chat_sdk_java.session.OpenAiSessionFactory;
import com.example.chat_sdk_java.session.defaults.DefaultOpenAiSessionFactory;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;


@SpringBootTest
public class ApiTest {
    private Logger log = LoggerFactory.getLogger(ApiTest.class);

    private OpenAiSession openAiSession;
    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("395f31326c14d475cb273747f76a80a0.wo6jyLZMFSGnkdqP");
        configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }
    @Test
    public void test_completions() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 入参；模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.GLM_3_5_TURBO); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setTools(new ArrayList<Tool>() {
            private static final long serialVersionUID = -7988151926241837899L;
            {
                add(Tool.builder()
                        .type(ToolType.web_search)
                        .webSearch(WebSearch.builder().enable(true).searchQuery("java").build())
                        .build());
            }
        });
        request.setMessages(new ArrayList<Message>() {
            private static final long serialVersionUID = -7988151926241837899L;
            {
                add(Message.builder()
                        .role(Role.user.getCode())
                        .content("1+1等于几")
                        .build());
            }
        });
        // 请求
        openAiSession.completions(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                log.info("测试结果 onEvent：{}", response.getChoices());
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getCode().equals(type)) {
                    Usage usage = JSON.parseObject(String.valueOf(response.getUsage()), Usage.class);
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(usage));
                }
            }
            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话完成");
                countDownLatch.countDown();
            }
            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                log.info("对话异常");
                countDownLatch.countDown();
            }
        });
        // 等待
        countDownLatch.await();
    }
    @Test
    public  void get_proDog() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ImageCompletionRequest imageCompletionRequest = new ImageCompletionRequest();
        imageCompletionRequest.setModel(Model.COGVIEW_3);
        imageCompletionRequest.setPrompt("画一个小狗");

        openAiSession.genImages(imageCompletionRequest);
        countDownLatch.await();
    }
}
