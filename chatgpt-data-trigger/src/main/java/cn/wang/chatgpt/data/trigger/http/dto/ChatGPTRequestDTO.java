package cn.wang.chatgpt.data.trigger.http.dto;

import cn.wang.chatgpt.data.types.enums.ChatGPTModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatGPTRequestDTO implements Serializable {
    /** 默认模型 */
    private  String model = ChatGPTModel.GLM_4V.getCode();
    /** 问题描述 */
    private List<MessageEntity> messages;
}
