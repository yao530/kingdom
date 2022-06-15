package com.ltu.model.request.smscode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class VerifySmsCodeReq {
    /**
     * 用户手机号
     */
    @ApiModelProperty(value="手机号，必传")
    @NotBlank(message = "手机号不能为空")
    private String userPhone;

    /**
     * 验证码
     */
    @ApiModelProperty(value="验证码，必传")
    @NotBlank(message = "验证码不能为空")
    private String smsCode;
}
