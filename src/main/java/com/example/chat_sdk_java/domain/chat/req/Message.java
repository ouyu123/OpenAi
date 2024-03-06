package com.example.chat_sdk_java.domain.chat.req;

import com.alibaba.fastjson.JSON;
import com.example.chat_sdk_java.domain.chat.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String role;
        private String content;

        Builder() {
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder content(Content content) {
            this.content = JSON.toJSONString(content);
            return this;
        }

        public Message build() {
            return new Message(this.role, this.content);
        }

        public String toString() {
            return "ChatCompletionRequest.Prompt.Builder(role=" + this.role + ", content=" + this.content + ")";
        }
    }
}