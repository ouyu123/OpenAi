package com.example.chat_sdk_java.domain.chat.res;


import com.example.chat_sdk_java.domain.chat.entity.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ChatCompletionResponse implements Serializable {

    // 24年1月发布的 GLM_3_5_TURBO、GLM_4 模型时新增
    private String id;
    private Long created;
    private List<Choice> choices;
    private Usage usage;


}
