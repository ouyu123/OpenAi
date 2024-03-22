package cn.wang.chatgpt.data.domain.openai.model.entity;

import cn.wang.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleLogicEntity<T> {
    private LogicCheckTypeVO type;
    private String info;
    private T data;
}
