package cn.wang.chatgpt.data.domain.openai.service;

import cn.wang.chatgpt.data.domain.auth.service.IAuthService;
import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.wang.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import cn.wang.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import cn.wang.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import cn.wang.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import cn.wang.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import cn.wang.chatgpt.data.types.common.Constants;
import cn.wang.chatgpt.data.types.exception.ChatGPTException;
import com.example.chat_sdk_java.session.OpenAiSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.reactive.GraphQlWebFluxAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Slf4j
public  abstract class  AbstractChatService implements  IChatService{

    @Resource
    protected OpenAiSession openAiSession;

    @Resource
    protected IOpenAiRepository openAiRepository;
    @Resource
    protected IAuthService authService;

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter,ChatProcessAggregate chatProcess)
    {
        try {
            // 1. 请求应答
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
            });
            emitter.onError(throwable -> log.error("流式问答请求错误，使用模型：{}", chatProcess.getModel(), throwable));
            //获取账户
            UserAccountQuotaEntity userAccountQuotaEntity =  openAiRepository.queryUserAccount(chatProcess.getOpenid());


            // 3. 规则过滤
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess,
                    userAccountQuotaEntity,
                    DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(),
                    DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.ACCOUNT_STATUS.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.MODEL_TYPE.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.USER_QUOTA.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode()
            );


            if (!LogicCheckTypeVO.SUCCESS.equals(ruleLogicEntity.getType())) {
                emitter.send(ruleLogicEntity.getInfo());
                emitter.complete();
                return emitter;
            }

            // 4. 应答处理
            this.doMessageResponse(ruleLogicEntity.getData(), emitter);

        }catch (Exception e)
        {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(),
                    Constants.ResponseCode.UN_ERROR.getInfo());

        }
        //返回结果
        return emitter;
    }
    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws Exception;
    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess,UserAccountQuotaEntity data,
                                                                          String... logic) throws Exception;
}
