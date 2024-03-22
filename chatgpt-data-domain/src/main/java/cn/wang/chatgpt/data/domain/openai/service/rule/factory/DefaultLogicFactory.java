package cn.wang.chatgpt.data.domain.openai.service.rule.factory;

import cn.wang.chatgpt.data.domain.openai.annotation.LogicStrategy;
import cn.wang.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultLogicFactory{

    public Map<String, ILogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    //自动装配 把实现了ILogicFilter接口的类加入到List<ILogicFilter>中
    public DefaultLogicFactory(List<ILogicFilter> logicFilters)
    {
        logicFilters.forEach(logic->{

            System.out.println(logic);
            //对当前迭代到的 ILogicFilter 对象的类进行检查，查找是否存在 LogicStrategy 注解。如果存在，则将该注解信息赋值给 strategy 变量。
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(),LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public Map<String, ILogicFilter> openLogicFilter() {
        return logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel{
        NULL("NULL", "放行不用过滤"),
        ACCESS_LIMIT("ACCESS_LIMIT", "访问次数过滤"),
        SENSITIVE_WORD("SENSITIVE_WORD", "敏感词过滤"),
        USER_QUOTA("USER_QUOTA", "用户额度过滤"),
        MODEL_TYPE("MODEL_TYPE", "模型可用范围过滤"),
        ACCOUNT_STATUS("ACCOUNT_STATUS", "账户状态过滤");
        private  String code;
        private  String info;
    }
}
