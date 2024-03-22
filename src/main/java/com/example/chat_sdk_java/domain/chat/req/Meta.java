package com.example.chat_sdk_java.domain.chat.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta implements Serializable {
    @JsonProperty("user_info")
    private  String userInfo;
    @JsonProperty("bot_info")
    private String botInfo;
    @JsonProperty("bot_name")
    private  String botName;
    @JsonProperty("user_name")
    private String userName;
}
