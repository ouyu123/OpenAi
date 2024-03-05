package com.example.chat_sdk_java.domain.chat.SynRes;

import com.example.chat_sdk_java.domain.chat.req.Message;
import com.example.chat_sdk_java.domain.chat.res.Delta;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
    private  int index;
    @JsonProperty("finish_reason")
    private String  finishReason;
    private Message message;
}
