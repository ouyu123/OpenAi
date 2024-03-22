package cn.wang.chatgpt.data.infrastructure.repository;


import cn.wang.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import cn.wang.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import cn.wang.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import cn.wang.chatgpt.data.infrastructure.dao.IUserAccountDao;
import cn.wang.chatgpt.data.infrastructure.po.UserAccountPO;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class OpenAiRepository implements IOpenAiRepository {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public int subAccountQuota(String openai) {

        return  userAccountDao.subAccountQuota(openai);

    }

    @Override
    public UserAccountQuotaEntity queryUserAccount(String openid) {
        System.out.println(userAccountDao.getClass().getName());
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if (null == userAccountPO) return null;
        UserAccountQuotaEntity userAccountQuotaEntity = new UserAccountQuotaEntity();
        userAccountQuotaEntity.setOpenid(userAccountPO.getOpenid());
        userAccountQuotaEntity.setTotalQuota(userAccountPO.getTotalQuota());
        userAccountQuotaEntity.setSurplusQuota(userAccountPO.getSurplusQuota());
        userAccountQuotaEntity.setUserAccountStatusVO(UserAccountStatusVO.get(userAccountPO.getStatus()));
        userAccountQuotaEntity.genModelTypes(userAccountPO.getModelTypes());
        return userAccountQuotaEntity;
    }
}
