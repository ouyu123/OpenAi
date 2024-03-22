package com.example.chat_sdk_java.domain.chat.vo;

import lombok.Getter;

@Getter
public enum ContentType {
    text("text", "文本"),
    image_url("image_url", "图"),
    json_string("json_string","JSON 字符串"),
    ;
    private final String code;
    private final String info;
    private ContentType(String code, String info)
    {
        this.code=code;
        this.info=info;
    }

}
