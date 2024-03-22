package cn.wang.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatGPTModel {

    GLM_4V("glm-4v"),

    ;
    private final String code;


}
