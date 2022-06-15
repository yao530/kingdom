package com.ltu.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdatePasswordReq extends CommonRequest {

	private static final long serialVersionUID = 4468655431209955093L;
	@NotBlank(message = "旧密码不能为空")
	private String id;

	@ApiModelProperty(required=true)
	@NotBlank(message = "旧密码不能为空")
	private String oldPassword;
	
	@ApiModelProperty(required=true)
	@NotBlank(message = "新密码不能为空")
	private String password;

}
