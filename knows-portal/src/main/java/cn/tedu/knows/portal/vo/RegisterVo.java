package cn.tedu.knows.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RegisterVo implements Serializable {

    //表示下面的属性不能为空   message错误提示信息
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp ="^1\\d{10}$",message = "手机号格式不正确")  //正则表达式  message当属性不符合正则表达式时的错误信息
    private String phone;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "昵称2~20个字符")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密码为6~20位数字字母'_'")
    private String password;

    @NotBlank(message = "密码确认不能为空")
    private String confirm;




}
