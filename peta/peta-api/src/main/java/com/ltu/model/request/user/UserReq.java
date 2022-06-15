package com.ltu.model.request.user;

import com.ltu.model.request.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserReq extends CommonRequest {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "用户id")
	private String id;

	@NotBlank(message = "sessionKey不能为空")
	private String sessionKey;

	@NotBlank(message = "encryptedData不能为空")
	private String encryptedData;

	@NotBlank(message = "iv不能为空")
	private String iv;
}

