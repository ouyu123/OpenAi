package com.example.chat_sdk_java.domain.chat.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoleCompletionRequest implements Serializable {

    private List<Message> prompt;

    private Meta meta;

    @JsonProperty("request_id")
    private String requestId=String.valueOf(System.currentTimeMillis()/1000L);

    @JsonProperty("return_type")
    private String returnType;

    private boolean incremental = true;


}
