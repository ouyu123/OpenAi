package com.example.chat_sdk_java;


import com.example.chat_sdk_java.domain.chat.SynRes.ChatCompletionSyncResponse;
import com.example.chat_sdk_java.domain.chat.req.ChatCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.ChatRoleCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.ImageCompletionRequest;
import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.res.ImageCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IOpenAiApi {
//    https://open.bigmodel.cn/api/paas/v4/chat/completions
//    https://open.bigmodel.cn/api/paas/v4/chat/completions
//    https://open.bigmodel.cn/api/paas/v4/chat/completions
    String v4 = "api/paas/v4/chat/completions";
    @POST(v4)
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Body ChatCompletionRequest chatCompletionRequest);

    @POST(v4)
    Single<ChatCompletionSyncResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);



    String cogview3 = "api/paas/v4/images/generations";

    @POST(cogview3)
    Single<ImageCompletionResponse> genImages(@Body ImageCompletionRequest imageCompletionRequest);

    String  charglm_3="api/paas/v3/model-api/charglm-3/sse-invoke";
    String charglmsyn_3="api/paas/v3/model-api/charglm-3/invoke";
    Single<ChatCompletionResponse> completions(@Body ChatRoleCompletionRequest chatRoleCompletionRequest);
}
