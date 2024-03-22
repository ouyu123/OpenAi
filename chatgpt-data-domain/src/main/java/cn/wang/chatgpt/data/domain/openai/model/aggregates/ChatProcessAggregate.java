package cn.wang.chatgpt.data.domain.openai.model.aggregates;

import cn.wang.chatgpt.data.domain.openai.model.entity.MessageEntity;
import cn.wang.chatgpt.data.types.common.Constants;
import cn.wang.chatgpt.data.types.enums.ChatGPTModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatProcessAggregate {


    /** 用户ID */
    private String openid;
    /** 默认模型 */
    private String model = ChatGPTModel.GLM_4V.getCode();
    /** 问题描述 */
    private List<MessageEntity> messages;

    //判断是不是在白名单中
    public  boolean isWhiteList(String whiteListStr)
    {
        String[] whiteList = whiteListStr.split((Constants.SPLIT));
        for (String whiteOpenid :whiteList
             ) {
            if(whiteOpenid.equals(openid)) return true;
        }
        return  false;
    }
}
