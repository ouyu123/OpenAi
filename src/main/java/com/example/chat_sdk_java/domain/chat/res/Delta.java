package com.example.chat_sdk_java.domain.chat.res;

import lombok.Data;

@Data
public class Delta {
    private String role;
    private String content;
}
