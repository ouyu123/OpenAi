package com.example.chat_sdk_java.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    /**
     * user 用户输入的内容，role位user
     */
    USER("user"),
    /**
     * 模型生成的内容，role位assistant
     */
    ASSISTANT("assistant"),

    /**
     * 系统
     */
    SYSTEM("system"),

    ;
    private final String code;

}