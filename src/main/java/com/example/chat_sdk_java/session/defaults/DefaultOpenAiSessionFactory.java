package com.example.chat_sdk_java.session.defaults;




import com.example.chat_sdk_java.IOpenAiApi;
import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.example.chat_sdk_java.executor.Executor;
import com.example.chat_sdk_java.interceptor.OpenAiHTTPInterceptor;
import com.example.chat_sdk_java.session.Configuration;
import com.example.chat_sdk_java.session.OpenAiSession;
import com.example.chat_sdk_java.session.OpenAiSessionFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description 会话工厂
 * @github https://github.com/fuzhengwei/chatglm-sdk-java
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory {
        private final Configuration configuration;

        public DefaultOpenAiSessionFactory(Configuration configuration) {
            this.configuration = configuration;
        }

        @Override
        public OpenAiSession openSession() {
            // 1. 日志配置
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(configuration.getLevel());

            // 2. 开启 Http 客户端
            OkHttpClient okHttpClient = new OkHttpClient
                    .Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new OpenAiHTTPInterceptor(configuration))
                    .connectTimeout(configuration.getConnectTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(configuration.getWriteTimeout(), TimeUnit.SECONDS)
                    .readTimeout(configuration.getReadTimeout(), TimeUnit.SECONDS)
                    .build();

            configuration.setOkHttpClient(okHttpClient);

            // 3. 创建 API 服务
            IOpenAiApi openAiApi = new Retrofit.Builder()
                    .baseUrl(configuration.getApiHost())
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build().create(IOpenAiApi.class);

            configuration.setOpenAiApi(openAiApi);

            // 4. 实例化执行器
            HashMap<Model, Executor> executorGroup = configuration.newExecutorGroup();

            return new DefaultOpenAiSession(configuration, executorGroup);
        }

}

