package com.example.chat_sdk_java.domain.chat.req;

import com.example.chat_sdk_java.domain.chat.entity.Function;
import com.example.chat_sdk_java.domain.chat.entity.Retrieval;
import com.example.chat_sdk_java.domain.chat.entity.WebSearch;
import com.example.chat_sdk_java.domain.chat.vo.ToolType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    private ToolType type;
    private Function function;
    private Retrieval retrieval;
    @JsonProperty("web_search")
    private WebSearch webSearch;

    public String getType() {
        return type.getCode();
    }
}
