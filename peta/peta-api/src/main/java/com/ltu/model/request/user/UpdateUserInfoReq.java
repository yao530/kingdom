package com.ltu.model.request.user;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserInfoReq{


    @ApiModelProperty(value = " e_mail:邮箱账号 ")
    private String identityFrontUrl;

    @ApiModelProperty(value = " user_nick:用户昵称 ")
    private String identityBackUrl;

    @ApiModelProperty(value = " 短信验证码 ")
    private String smsCode;

    @ApiModelProperty(value = " real_name:真实姓名 ")
    private String realName;

    @ApiModelProperty(value = " identity_id:身份证ID ")
    private String identityId;

}
