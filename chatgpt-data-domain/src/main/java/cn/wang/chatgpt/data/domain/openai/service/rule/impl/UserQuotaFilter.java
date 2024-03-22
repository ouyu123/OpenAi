package cn.wang.chatgpt.data.domain.openai.service.rule.impl;

import cn.wang.chatgpt.data.domain.openai.annotation.LogicStrategy;
import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.wang.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import cn.wang.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import cn.wang.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import cn.wang.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import cn.wang.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import cn.wang.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Slf4j
@Component
@LogicStrategy(logicMode= DefaultLogicFactory.LogicModel.USER_QUOTA)
public class UserQuotaFilter implements ILogicFilter<UserAccountQuotaEntity> {


    @Resource
    private IOpenAiRepository openAiRepository;
    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        if(data.getSurplusQuota() > 0)
        {
            // 扣减账户额度；因为是个人账户数据，无资源竞争，所以直接使用数据库也可以。但为了效率，也可以优化为 Redis 扣减。
            int updateCount = openAiRepository.subAccountQuota(data.getOpenid());

            if(0 != updateCount )
            {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
            }
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.REFUSE)
                    .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！").data(chatProcess).build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.REFUSE)
                .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！").data(chatProcess).build();
    }
}
