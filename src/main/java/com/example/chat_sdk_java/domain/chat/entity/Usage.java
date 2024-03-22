package com.example.chat_sdk_java.domain.chat.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Usage implements Serializable {
    private int completion_tokens;
    private int prompt_tokens;
    private int total_tokens;
}