package com.example.chat_sdk_java.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSearch {
    // 是否启用搜索，默认启用搜索 enable = true/false
    private Boolean enable = true;
    // 强制搜索自定义关键内容，此时模型会根据自定义搜索关键内容返回的结果作为背景知识来回答用户发起的对话。
    @JsonProperty("search_query")
    private String searchQuery;
}
