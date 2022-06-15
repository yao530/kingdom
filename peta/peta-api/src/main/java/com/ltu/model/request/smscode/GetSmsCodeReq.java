package com.ltu.model.request.smscode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GetSmsCodeReq {
    /**
     * 用户手机号
     */
    @ApiModelProperty(value="手机号，必传", required = true)
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11,max = 11, message = "手机号码格式不正确")
    private String userPhone;

    /**
     * 图形验证码
     */
    @ApiModelProperty(value="图形验证码，必传", required = true)
    @NotBlank(message = "图形验证码不能为空")
    private String kaptcha;

    /**
     * 图形验证码
     */
    @ApiModelProperty(value="图形验证码对应的UUID，以UUID形式获取时必传，其他可忽略")
    private String kaptchaUUID;
}
