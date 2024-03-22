package cn.wang.chatgpt.data.domain.openai.service.rule.impl;

import cn.wang.chatgpt.data.domain.openai.annotation.LogicStrategy;
import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.wang.chatgpt.data.domain.openai.model.entity.MessageEntity;
import cn.wang.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import cn.wang.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import cn.wang.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import cn.wang.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import cn.wang.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveWordFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Resource
    private  SensitiveWordBs words;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess,UserAccountQuotaEntity data) throws Exception {
        // 1. 白名单用户直接放行
        if(chatProcess.isWhiteList(whiteListStr))
        {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
        }
        ChatProcessAggregate newChatProcessAggregate = new ChatProcessAggregate();

        newChatProcessAggregate.setOpenid(chatProcess.getOpenid());
        newChatProcessAggregate.setModel(chatProcess.getModel());
        List<MessageEntity> newMessages = chatProcess.getMessages().stream()
                .map(message -> {
                    String content = message.getContent();
                    String replace = words.replace(content);
                    return MessageEntity.builder()
                            .role(message.getRole())
                            .name(message.getName())
                            .content(replace)
                            .build();
                })
                .collect(Collectors.toList());

        newChatProcessAggregate.setMessages(newMessages);
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(newChatProcessAggregate)
                .build();
    }
}
