package com.example.chat_sdk_java.domain.chat.vo;


import lombok.Getter;

@Getter
public enum Model {
    /** @deprecated */
    GLM_3_5_TURBO("glm-3-turbo", "适用于对知识量、推理能力、创造力要求较高的场景"),
    GLM_4("glm-4", "适用于复杂的对话交互和深度内容创作设计的场景"),
    GLM_4V("glm-4v", "根据输入的自然语言指令和图像信息完成任务，推荐使用 SSE 或同步调用方式请求接口"),
    COGVIEW_3("cogview-3", "根据用户的文字描述生成图像,使用同步调用方式请求接口");

    private final String code;
    private final String info;

    private Model(String code, String info) {
        this.code = code;
        this.info = info;
    }
}

