package com.example.chat_sdk_java.domain.chat.SynRes;


import com.example.chat_sdk_java.domain.chat.entity.Usage;

import java.util.List;

public class ChatCompletionSyncResponse {

    private Integer id;
    private long created;
    private String model;

    // 24年1月发布模型新增字段 GLM3、GLM4
    private Usage usage;
    private List<Choice> choices;


}
