package cn.wang.chatgpt.data.domain.auth.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AuthTypeVo {
    A0000("0000","验证成功"),
    A0001("0001","验证码不存在"),
    A0002("0002","验证码失效");
    private String code;
    private String info;
}
