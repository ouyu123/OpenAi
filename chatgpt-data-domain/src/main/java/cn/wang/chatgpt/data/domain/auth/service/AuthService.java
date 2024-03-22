package cn.wang.chatgpt.data.domain.auth.service;

import cn.wang.chatgpt.data.domain.auth.model.entity.AuthStateEntity;
import cn.wang.chatgpt.data.domain.auth.model.valobj.AuthTypeVo;
import com.google.common.cache.Cache;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService extends AbstractAuthService{


    @Resource
    private Cache<String,String> codeCache;

    @Override
    public AuthStateEntity checkCode(String code) {
        //获取验证码校验
        String openId =codeCache.getIfPresent(code);
        if(StringUtils.isBlank(openId))
        {

            return AuthStateEntity.builder()
                    .code(AuthTypeVo.A0001.getCode())
                    .info(AuthTypeVo.A0001.getInfo())
                    .build();
        }
        //移除缓存key值
        codeCache.invalidate(openId);
        codeCache.invalidate(code);

        //验证码校验成功
        return AuthStateEntity.builder()
                .code(AuthTypeVo.A0000.getCode())
                .info(AuthTypeVo.A0000.getInfo())
                .openId(openId)
                .build();
    }

    @Override
    public boolean checkToken(String token) {

        return isVerify(token);
    }

    public  String openid(String token)
    {
        Claims claims = decode(token);
        return claims.get("openId").toString();
    }
}
