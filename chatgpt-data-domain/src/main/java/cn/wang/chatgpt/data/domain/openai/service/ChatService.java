package cn.wang.chatgpt.data.domain.openai.service;

import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.wang.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import cn.wang.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import cn.wang.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import cn.wang.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import cn.wang.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import cn.wang.chatgpt.data.types.exception.ChatGPTException;
import com.alibaba.fastjson2.JSON;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.Message;
import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.res.Choice;
import com.example.chat_sdk_java.domain.chat.res.Delta;
import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.example.chat_sdk_java.domain.chat.vo.Role;
import jakarta.annotation.Resource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class ChatService extends  AbstractChatService{
    @Resource
    private DefaultLogicFactory logicFactory;
    public  void doMessageResponse(ChatProcessAggregate chatProcessAggregate, ResponseBodyEmitter emitter) throws Exception {
        // 1. 请求消息
        List<Message> messages = chatProcessAggregate.getMessages().stream()
                .map(entity-> Message.builder().
                        role(Role.valueOf(entity.getRole().toUpperCase()).getCode())
                        .content(entity.getContent()).build()).toList();
        // 2. 封装参数
        String[] parts = chatProcessAggregate.getModel().split("-");
        String output = parts[0].toUpperCase()+"_"+parts[1].toUpperCase();

        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setModel(Model.valueOf(output));
        chatCompletionRequest.setMessages(messages);
        chatCompletionRequest.setStream(true);
        // 3.2 请求应答
        System.out.println("new"+chatCompletionRequest);
        openAiSession.completions(chatCompletionRequest, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<Choice> choices = chatCompletionResponse.getChoices();
                for (Choice choice : choices) {
                    Delta delta = choice.getDelta();
//                    if (Role.ASSISTANT.getCode().equals(delta.getRole())) continue;

                    // 应答完成
                    String finishReason = choice.getFinishReason();
                    if (StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)) {
                        emitter.complete();
                        break;
                    }

                    // 发送信息
                    try {
                        emitter.send(delta.getContent());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }

    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data,
                                                                 String... logic) throws Exception {
        Map<String, ILogicFilter> logicFilterMap = logicFactory.openLogicFilter();
        RuleLogicEntity<ChatProcessAggregate> entity = null;

        for (String code: logic ) {

            if(DefaultLogicFactory.LogicModel.NULL.getCode().equals(code)) continue;

            entity = logicFilterMap.get(code).filter(chatProcess,data);
            if(!LogicCheckTypeVO.SUCCESS.equals(entity.getType())) return  entity;

        }
        return entity !=null ? entity : RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
    }

}
