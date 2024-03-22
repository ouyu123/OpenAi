package cn.wang.chatgpt.data.domain.openai.model.entity;

import cn.wang.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import cn.wang.chatgpt.data.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountQuotaEntity {

    /**
     * 用户ID
     */
    private  String openid;
    /**
     * 总量额度
     */
    private Integer totalQuota;
    /**
     * 剩余额度
     */
    private Integer surplusQuota;
    /**
     * 账户状态
     */
    private UserAccountStatusVO userAccountStatusVO;
    /**
     * 模型类型；一个卡支持多个模型调用，这代表了允许使用的模型范围
     */
    private List<String> allowModelTypeList;

    public  void genModelTypes(String modelTypes)
    {
        String[] vals =modelTypes.split(Constants.SPLIT);
        this.allowModelTypeList = Arrays.asList(vals);
    }
}
