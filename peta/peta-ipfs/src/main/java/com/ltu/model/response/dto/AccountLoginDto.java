package com.ltu.model.response.dto;

   
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountLoginDto {


    @ApiModelProperty(value="账号名称")
    private String accountName;

    @ApiModelProperty(value="logo")
    private String accountLogo;

    @ApiModelProperty(value="角色名称")
    private String roleName;

    @ApiModelProperty(value="登陆令牌")
    private String token;

}
