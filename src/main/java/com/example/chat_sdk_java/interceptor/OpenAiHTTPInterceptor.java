package com.example.chat_sdk_java.interceptor;

import com.example.chat_sdk_java.session.Configuration;
import com.example.chat_sdk_java.utils.BearerTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
@Slf4j
public class OpenAiHTTPInterceptor implements  Interceptor {

    /**
     * 智普Ai，Jwt加密Token
     */
    private final Configuration configuration;

    public OpenAiHTTPInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull Response intercept(Interceptor.Chain chain) throws IOException {
        // 1. 获取原始 Request
        Request original = chain.request();
        // 2. 构建请求
        Request request = original.newBuilder()
                .url(original.url())
// BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret())
                .header("Authorization", "Bearer " +BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .header("Content-Type", Configuration.APPLICATION_JSON)
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
//                .header("Accept", null != original.header("Accept") ? original.header("Accept") : Configuration.SSE_CONTENT_TYPE)
                .method(original.method(), original.body())
                .build();
        log.info("拦截前的请求数据{}",original);
        log.info("拦截后的请求数据{},{}",request.headers(),request.body());
        // 3. 返回执行结果
        return chain.proceed(request);
    }
}
