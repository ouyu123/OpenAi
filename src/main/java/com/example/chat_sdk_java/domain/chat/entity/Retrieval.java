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
public class Retrieval {
    // 当涉及到知识库ID时，请前往开放平台的知识库模块进行创建或获取。
    @JsonProperty("knowledge_id")
    private String knowledgeId;
    // 请求模型时的知识库模板，默认模板：
    @JsonProperty("prompt_template")
    private String promptTemplate = "\"\"\"\n" +
            "{{ knowledge}}\n" +
            "\"\"\"\n" +
            "中找问题\n" +
            "\"\"\"\n" +
            "{{question}}\n" +
            "\"\"\"";
}
