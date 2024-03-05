package com.example.chat_sdk_java.domain.chat.res;

import com.example.chat_sdk_java.domain.chat.entity.Usage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class Meta {
    private String task_status;
    private Usage usage;
    private String task_id;
    private String request_id;
}