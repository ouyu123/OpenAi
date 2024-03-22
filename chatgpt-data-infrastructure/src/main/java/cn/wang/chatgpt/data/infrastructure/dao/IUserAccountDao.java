package cn.wang.chatgpt.data.infrastructure.dao;

import cn.wang.chatgpt.data.infrastructure.po.UserAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface IUserAccountDao {
    int subAccountQuota(String openid);

    UserAccountPO queryUserAccount(String openid);

    int addAccountQuota(UserAccountPO userAccountPOReq);

    void insert(UserAccountPO userAccountPOReq);
}
