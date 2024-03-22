package com.example.chat_sdk_java.domain.chat.res;

import com.example.chat_sdk_java.domain.chat.entity.Usage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoleCompletionResponse implements Serializable {
    private  String request_id;
    private String task_id;
    private  String task_status;
    private Usage usage;

}
