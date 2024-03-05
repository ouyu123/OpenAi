package com.example.chat_sdk_java.domain.chat.req;





import com.example.chat_sdk_java.domain.chat.vo.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ChatCompletionRequest implements Serializable {
    private Boolean isCompatible = true;
    private Model model= Model.GLM_3_5_TURBO;

    private List<Message> messages;
    @JsonProperty("request_id")
    private String requestId= String.format("wn-%d", System.currentTimeMillis());;
    @JsonProperty("do_sample")
    private boolean doSample=true;

    private boolean stream=true;
    private float temperature=0.9f;
    @JsonProperty("top_p")
    private float topP=0.7f;

    @JsonProperty("max_tokens")
    private Integer maxTokens=2048;

    private List<String> stop;

    private List<Tool> tools;

    @JsonProperty("tool_choice")
    private String toolChoice = "auto";


    @Override
    public String toString() {
        try {
            // 24年1月发布新模型后调整
              Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("model", this.model.getCode());
                if (null == this.messages ) {
                    throw new RuntimeException("One of messages or prompt must not be empty！");
                }
                paramsMap.put("messages", this.messages);
                if (null != this.requestId) {
                    paramsMap.put("request_id", this.requestId);
                }

                paramsMap.put("do_sample", this.doSample);
                paramsMap.put("stream", this.stream);
                paramsMap.put("temperature", this.temperature);
                paramsMap.put("top_p", this.topP);
                paramsMap.put("max_tokens", this.maxTokens);
                if (null != this.stop && this.stop.size() > 0) {
                    paramsMap.put("stop", this.stop);
                }
                if (null != this.tools && this.tools.size() > 0) {
                    paramsMap.put("tools", this.tools);
                    paramsMap.put("tool_choice", this.toolChoice);
                }
                return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
