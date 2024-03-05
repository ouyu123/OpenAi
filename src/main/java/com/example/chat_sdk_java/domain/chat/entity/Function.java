package com.example.chat_sdk_java.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Function {
    // 函数名称，只能包含a-z，A-Z，0-9，下划线和中横线。最大长度限制为64
    private String name;
    // 用于描述函数功能。模型会根据这段描述决定函数调用方式。
    private String description;
    // parameter 字段需要传入一个 Json Schema 对象，以准确地定义函数所接受的参数。https://open.bigmodel.cn/dev/api#glm-3-turbo
    private Object parameters;
}
