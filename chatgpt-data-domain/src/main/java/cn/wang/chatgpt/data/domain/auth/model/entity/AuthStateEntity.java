package cn.wang.chatgpt.data.domain.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthStateEntity {

    private String code;
    private  String info;
    private  String openId;
    private  String token;
}
