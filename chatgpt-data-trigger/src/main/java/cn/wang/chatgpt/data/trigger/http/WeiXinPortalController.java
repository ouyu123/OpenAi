package cn.wang.chatgpt.data.trigger.http;



import cn.wang.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import cn.wang.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import cn.wang.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import cn.wang.chatgpt.data.domain.weixin.service.IWeiXinValidateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import cn.wang.chatgpt.data.types.sdk.weixin.XmlUtil;

import java.util.Date;

/**
 * @author
 * @description 微信公众号，验签和请求应答
 * @create 2023-08-05 16:50
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/${app.config.api-version}/wx/portal/{appid}")
public class WeiXinPortalController {

    @Resource
    private IWeiXinValidateService weiXinValidateService;

    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;
    /**
     * 处理微信服务器发来的get请求，进行签名的验证【apix.natapp1.cc 是我在 <a href="https://natapp.cn/">https://natapp.cn</a> 购买的渠道，你需要自己购买一个使用】
     * <a href="http://apix.natapp1.cc/api/v1/wx/portal/wxad979c0307864a66">http://apix.natapp1.cc/api/v1/wx/portal/wxad979c0307864a66</a>
     * <p>
     * appid     微信端AppID
     * signature 微信端发来的签名
     * timestamp 微信端发来的时间戳
     * nonce     微信端发来的随机字符串
     * echostr   微信端发来的验证字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "echostr", required = false) String echostr,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce)
    {
        try {
            log.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            boolean check = weiXinValidateService.checkSign(signature, timestamp, nonce);
            log.info("微信公众号验签信息{}完成 check：{}", appid, check);
            if (!check) {
                return null;
            }
            return echostr;
        }
        catch (Exception e)
        {
            log.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(
                       @PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       HttpServletRequest request) {
        try {

            log.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);
            System.out.println("appid"+appid+" signature" +signature +" openid"+openid);
            // 消息转换
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody,MessageTextEntity.class);
            System.out.println(String.format("openId %s fromUserName %s",openid,message.getFromUserName()));
            // 构建实体
            UserBehaviorMessageEntity entity = UserBehaviorMessageEntity.builder()
                    .openId(openid)
                    .fromUserName(message.getFromUserName())
                    .msgType(message.getMsgType())
                    .content(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim())
                    .createTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L))
                    .build();

            // 受理消息
            String result = weiXinBehaviorService.acceptUserBehavior(entity);
            log.info("接收微信公众号信息请求{}完成 {}", openid, result);
            return result;
        } catch (Exception e) {
            log.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }
}
