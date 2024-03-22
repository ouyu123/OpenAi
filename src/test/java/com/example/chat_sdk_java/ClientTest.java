package com.example.chat_sdk_java;

import com.alibaba.fastjson.JSON;
import com.example.chat_sdk_java.domain.chat.entity.Usage;
import com.example.chat_sdk_java.domain.chat.req.ChatRoleCompletionRequest;
import com.example.chat_sdk_java.domain.chat.req.Message;
import com.example.chat_sdk_java.domain.chat.req.Meta;
import com.example.chat_sdk_java.domain.chat.res.ChatCompletionResponse;
import com.example.chat_sdk_java.domain.chat.res.ChatRoleCompletionResponse;
import com.example.chat_sdk_java.domain.chat.vo.ContentType;
import com.example.chat_sdk_java.domain.chat.vo.EventType;
import com.example.chat_sdk_java.domain.chat.vo.Role;
import com.example.chat_sdk_java.session.Configuration;
import com.example.chat_sdk_java.session.OpenAiSession;
import com.example.chat_sdk_java.session.OpenAiSessionFactory;
import com.example.chat_sdk_java.session.defaults.DefaultOpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ClientTest {

    public static void main(String[] args) throws Exception {
            OpenAiSession openAiSession;
            Configuration configuration = new Configuration();
            configuration.setApiHost("https://open.bigmodel.cn/");
            configuration.setApiSecretKey("395f31326c14d475cb273747f76a80a0.wo6jyLZMFSGnkdqP");
            configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
            // 2. 会话工厂
            OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
            // 3. 开启会话
            openAiSession = factory.openSession();
        ChatRoleCompletionRequest chatRoleCompletionRequest =new ChatRoleCompletionRequest();
        Meta meta = Meta.builder()
                .userName("陆星辰")
                .userInfo("我是陆星辰，是一个男性，是一位知名导演，也是苏梦远的合作导演。我擅长拍摄音乐题材的电影。苏梦远对我的态度是尊敬的，并视我为良师益友。")
                .botInfo("苏梦远，本名苏远心，是一位当红的国内女歌手及演员。在参加选秀节目后，凭借独特的嗓音及出众的舞台魅力迅速成名，进入娱乐圈。她外表美丽动人，但真正的魅力在于她的才华和勤奋。苏梦远是音乐学院毕业的优秀生，善于创作，拥有多首热门原创歌曲。除了音乐方面的成就，她还热衷于慈善事业，积极参加公益活动，用实际行动传递正能量。在工作中，她对待工作非常敬业，拍戏时总是全身心投入角色，赢得了业内人士的赞誉和粉丝的喜爱。虽然在娱乐圈，但她始终保持低调、谦逊的态度，深得同行尊重。在表达时，苏梦远喜欢使用“我们”和“一起”，强调团队精神。")
                .botName("苏梦远")
                .build();
        chatRoleCompletionRequest.setMeta(meta);
        chatRoleCompletionRequest.setPrompt(new ArrayList<Message>(){
            private static final long serialVersionUID = -7948111926241837899L;
            {
                add(Message.builder().role(Role.ASSISTANT.getCode())
                        .content("旁白：苏梦远主演了陆星辰导演的一部音乐题材电影，在拍摄期间，两人因为一场戏的表现有分歧。） 导演，关于这场戏，我觉得可以尝试从角色的内心情感出发，让表现更加真实。")
                        .build());
            }
        });

        openAiSession.completions(chatRoleCompletionRequest, new EventSourceListener() {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                @Override
                public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                    log.info("测试结果 onEvent：{}", data);
////                     type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                    if (EventType.finish.getCode().equals(type)) {
                        log.info("[输入结束]");
                    }
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    log.info("对话完成");
                    countDownLatch.countDown();
                }

                @Override
                public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                    log.info("对话异常");
                    countDownLatch.countDown();
                }
            });
        }

}
