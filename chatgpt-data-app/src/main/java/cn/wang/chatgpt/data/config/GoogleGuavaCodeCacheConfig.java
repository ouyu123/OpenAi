package cn.wang.chatgpt.data.config;

import cn.wang.chatgpt.data.trigger.mq.OrderPaySuccessListener;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
public class GoogleGuavaCodeCacheConfig {

    @Bean(name = "codeCache")
    public Cache<String,String> codeCache()
    {
        return  CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Bean(name="visitCache")
    public Cache<String,String> visitCache()
    {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3,TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener){
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }
}
