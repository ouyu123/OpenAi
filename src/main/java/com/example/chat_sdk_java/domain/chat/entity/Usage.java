package com.example.chat_sdk_java.domain.chat.entity;

import lombok.Data;

@Data
public class Usage {
    private int completion_tokens;
    private int prompt_tokens;
    private int total_tokens;
}