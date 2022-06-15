package com.ltu.model.request.user;

   
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class UserRegisterReq {
    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户姓名，必传", required = true)
    @NotBlank(message = "用户姓名不能为空")
    private String nickname;
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
    @ApiModelProperty(value="密码，密码登录时必传", required = true)
    private String password="";
    /**
     * 企业名称
     */
    @ApiModelProperty(value="企业名称，必传", required = true)
    @NotBlank(message = "企业名称不能为空")
    private String companyName;
    /**
     * 用户头像
     */
    @ApiModelProperty(value="用户头像，选传")
    private String userAvatar;
    /**
     * 用户简介
     */
    @ApiModelProperty(value="简介，选传")
    private String description;
}
