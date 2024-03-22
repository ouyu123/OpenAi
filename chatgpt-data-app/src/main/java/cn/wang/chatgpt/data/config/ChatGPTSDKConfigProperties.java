package cn.wang.chatgpt.data.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "chatgpt.sdk.config", ignoreInvalidFields = true)
public class ChatGPTSDKConfigProperties {
/**
 *       # 官网地址 https://api.openai.com/
 *       apiHost: https://open.bigmodel.cn/
 *       # 官网申请 https://platform.openai.com/account/api-keys
 *       apiKey: 395f31326c14d475cb273747f76a80a0.wo6jyLZMFSGnkdqP
 *       # 自主申请 http://api.xfg.im:8080/authorize?username=xfg&password=123 - 有时效性，主要为了大家学习使用
 *      /
    /** 转发地址 <a href="https://api.xfg.im/b8b6/">https://api.xfg.im/b8b6/</a> */
    private String apiHost;
    /** 可以申请 sk-*** */
    private String apiKey;
    /** 获取Token <a href="http://api.xfg.im:8080/authorize?username=xfg&password=123">访问获取</a> */
    private String authToken;
}
