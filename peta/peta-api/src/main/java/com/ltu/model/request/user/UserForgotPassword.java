package com.ltu.model.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserForgotPassword {
    /**
     * 用户手机号
     */
    @ApiModelProperty(value="手机号，必传", required = true)
    @NotBlank(message = "手机号不能为空")
    private String userPhone;
    /**
     * 短信验证码
     */
    @ApiModelProperty(value="验证码，必传", required = true)
    @NotBlank(message = "验证码不能为空")
    private String smsCode;
    /**
     * 登陆密码
     */
    @ApiModelProperty(value="密码，必传", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
