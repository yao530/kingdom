package com.ltu.model.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class UserResetPasswordReq {
    /**
     * 用户ID
     */
    @ApiModelProperty(value="userId，必传")
    @NotBlank(message = "userId不能为空")
    private String userId;
    /**
     * 旧密码
     */
    @ApiModelProperty(value="旧密码，必传")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @ApiModelProperty(value="新密码，必传")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
