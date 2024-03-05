package com.example.chat_sdk_java.domain.chat.entity;

import com.example.chat_sdk_java.domain.chat.entity.ImageUrl;
import com.example.chat_sdk_java.domain.chat.vo.ContentType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Content {
    private String type = ContentType.text.getCode();
    private String text;
    @JsonProperty("image_url")
    private ImageUrl imageUrl;
}
