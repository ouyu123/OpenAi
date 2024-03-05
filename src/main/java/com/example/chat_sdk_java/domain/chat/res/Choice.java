package com.example.chat_sdk_java.domain.chat.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Choice {
    private  int index;
    @JsonProperty("finish_reason")
    private String  finishReason;
    private Delta delta;



}
