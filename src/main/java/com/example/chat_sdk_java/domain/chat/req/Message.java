package com.example.chat_sdk_java.domain.chat.req;

import com.alibaba.fastjson.JSON;
import com.example.chat_sdk_java.domain.chat.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;

    public static PromptBuilder builder() {
        return new PromptBuilder();
    }

    public static class PromptBuilder {
        private String role;
        private String content;

        PromptBuilder() {
        }

        public PromptBuilder role(String role) {
            this.role = role;
            return this;
        }

        public PromptBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PromptBuilder content(Content content) {
            this.content = JSON.toJSONString(content);
            return this;
        }

        public Message build() {
            return new Message(this.role, this.content);
        }

        public String toString() {
            return "ChatCompletionRequest.Prompt.PromptBuilder(role=" + this.role + ", content=" + this.content + ")";
        }
    }
}