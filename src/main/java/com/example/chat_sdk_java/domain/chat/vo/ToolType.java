package com.example.chat_sdk_java.domain.chat.vo;

import lombok.Getter;

@Getter
public enum ToolType {
    function("function", "函数功能"),
    retrieval("retrieval", "知识库"),
    web_search("web_search", "联网"),
    ;
    private final String code;
    private final String info;
    private ToolType(String code, String info)
    {
        this.code=code;
        this.info=info;
    }

}
