package cn.wang.chatgpt.data.domain.openai.service;

import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


public interface IChatService {

    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter,ChatProcessAggregate chatProcessAggregate);
}
