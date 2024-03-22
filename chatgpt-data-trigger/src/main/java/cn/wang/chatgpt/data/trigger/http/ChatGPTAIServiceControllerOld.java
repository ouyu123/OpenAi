package cn.wang.chatgpt.data.trigger.http;

import cn.wang.chatgpt.data.trigger.http.dto.ChatGPTRequestDTO;
import cn.wang.chatgpt.data.types.exception.ChatGPTException;
import com.alibaba.fastjson2.JSON;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.Message;
import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.res.Choice;
import com.example.chat_sdk_java.domain.chat.res.Delta;
import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.example.chat_sdk_java.domain.chat.vo.Role;
import com.example.chat_sdk_java.session.OpenAiSession;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v0/")
public class ChatGPTAIServiceControllerOld {
    @Resource
    private OpenAiSession openAiSession;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    /**
     * 流式问题，ChatGPT 请求接口
     * <p>
     * curl -X POST \
     * http://localhost:8080/api/v1/chat/completions \
     * -H 'Content-Type: application/json;charset=utf-8' \
     * -H 'Authorization: b8b6' 395f31326c14d475cb273747f76a80a0.wo6jyLZMFSGnkdqP\
     * -d '{
     * "messages": [
     * {
     * "content": "写一个java冒泡排序",
     * "role": "user"
     * }
     * ],
     * "model": "gpt-3.5-turbo"
     * }'
     */

    @RequestMapping( value = "chat/completions", method = RequestMethod.POST)
    public ResponseBodyEmitter  completionsStream(@RequestBody ChatGPTRequestDTO request,
                                                  @RequestHeader("Authorization") String token,
                                                  HttpServletResponse response)
    {
        log.info("流式问答请求开始，使用的模型：{} 请求信息：{}",request.getModel()
                , JSON.toJSONString(request.getMessages()));
        try {
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control","no-Cache");
            if(!token.equals("b8b6"))
            {
                throw  new RuntimeException();
            }
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(3*60*1000L);//超时为3分钟
            emitter.onCompletion(()->{
                log.info("流式问答请求完成，使用模型{}",request.getModel());
            });
            emitter.onError((throwable)->{
                log.error("流式问答请求错误，使用模型：{}",request.getModel(),throwable);
            });
            System.out.println(request.getMessages());
            List<Message> messages = request.getMessages().stream()
                    .map(entity-> Message.builder().
                            role(Role.valueOf(entity.getRole().toUpperCase()).getCode())
                            .content(entity.getContent()).build()).toList();

            ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
            chatCompletionRequest.setModel(Model.GLM_4);
            chatCompletionRequest.setMessages(messages);
            chatCompletionRequest.setStream(true);

            System.out.println("old"+chatCompletionRequest);
            openAiSession.completions(chatCompletionRequest, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                    ChatCompletionResponse chatCompletionResponse =
                            JSON.parseObject(data,ChatCompletionResponse.class);
                    List<Choice> choices = chatCompletionResponse.getChoices();
                    for (Choice choice: choices) {
                        Delta delta = choice.getDelta();
//                        if(Role.ASSISTANT.getCode().equals(delta.getRole())) continue;
                        String finishReason = choice.getFinishReason();
                        if(StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason))
                        {
                            emitter.complete();
                            break;
                        }
                        try {
                            emitter.send(delta.getContent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            return emitter;
        }catch (Exception e)
        {
            log.error("流式应答，请求模型：{} 发生异常", request.getModel(), e);
            throw new ChatGPTException(e.getMessage());
        }


    }
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public ResponseBodyEmitter completionsStream(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        threadPoolExecutor.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    emitter.send("strdddddddddddddddd\r\n" + i);
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            emitter.complete();
        });

        return emitter;
    }

}
