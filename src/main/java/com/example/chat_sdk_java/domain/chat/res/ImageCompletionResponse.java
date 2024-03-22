package com.example.chat_sdk_java.domain.chat.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImageCompletionResponse implements Serializable {

    /**
     * 请求创建时间，是以秒为单位的Unix时间戳。
     */
    private Long created;

    private List<Image> data;

    @Data
    public static class Image{
        private String url;
    }

}
