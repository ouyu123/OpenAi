package cn.wang.chatgpt.data.trigger.http;

import cn.wang.chatgpt.data.domain.auth.service.IAuthService;
import cn.wang.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.wang.chatgpt.data.domain.openai.model.entity.MessageEntity;
import cn.wang.chatgpt.data.domain.openai.service.IChatService;
import cn.wang.chatgpt.data.trigger.http.dto.ChatGPTRequestDTO;
import cn.wang.chatgpt.data.types.common.Constants;
import cn.wang.chatgpt.data.types.exception.ChatGPTException;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatgpt")
public class ChatGPTAIServiceController {

    @Resource
    private IChatService chatService;
    @Resource
    private IAuthService authService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping(value = "/chat/completions",method = RequestMethod.POST)
    public ResponseBodyEmitter completionsStream(@RequestBody ChatGPTRequestDTO request,
                                                 @RequestHeader("Authorization") String token,
                                                 HttpServletResponse response)
    {
        log.info("流式问答请求开始，使用的模型：{}，请求信息：{}",request.getModel(), JSON.toJSONString(request.getMessages()));
        try {
            response.setContentType("text/event-stream");
            response.setHeader("Cache-Control","no-cache");
            response.setCharacterEncoding("UTF-8");
            // 2. 构建异步响应对象【对 Token 过期拦截】

            ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
            boolean success = authService.checkToken(token);

            if(!success)
            {
                try {
                    emitter.send(Constants.ResponseCode.TOKEN_ERROR.getCode());
                }catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                emitter.complete();
                return emitter;
            }
            //3.获取openid
            String openid = authService.openid(token);
            log.info("流式问答请求处理，openid:{} 请求模型:{}", openid, request.getModel());

            //4.构建参数
            ChatProcessAggregate chatProcessAggregate = ChatProcessAggregate.builder()
                    .openid(openid)
                    .messages(request.getMessages().stream().map(entity->
                                MessageEntity.builder().role(entity.getRole())
                                        .content(entity.getContent())
                                        .name(entity.getName())
                                        .build()).toList())
                    .model(request.getModel())
                    .build();
        // 5. 请求结果&返回
            return chatService.completions(emitter,chatProcessAggregate);


        }catch (Exception e)
        {
            log.error("流式应答，请求模型：{} 发生异常", request.getModel(), e);
            throw new ChatGPTException(e.getMessage());
        }
    }



    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public ResponseBodyEmitter completionsStream(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        threadPoolExecutor.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    emitter.send("strdddddddddddddddd\r\n" + i);
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            emitter.complete();
        });

        return emitter;
    }

}
