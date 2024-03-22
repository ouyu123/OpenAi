package cn.wang.chatgpt.data.config;

import com.example.chat_sdk_java.session.OpenAiSession;
import com.example.chat_sdk_java.session.OpenAiSessionFactory;
import com.example.chat_sdk_java.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ChatGPTSDKConfigProperties.class)
public class ChatGPTSDKConfig {

    @Bean
    public OpenAiSession openAiSession(ChatGPTSDKConfigProperties chatGPTSDKConfigProperties)
    {
        com.example.chat_sdk_java.session.Configuration configuration =new com.example.chat_sdk_java.session.Configuration();
        configuration.setApiHost(chatGPTSDKConfigProperties.getApiHost());
        configuration.setApiSecretKey(chatGPTSDKConfigProperties.getApiKey());
        DefaultOpenAiSessionFactory openAiSessionFactory = new DefaultOpenAiSessionFactory(configuration);
        return openAiSessionFactory.openSession();
    }

}
