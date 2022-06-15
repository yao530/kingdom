package com.ltu.model.request.user;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public class WxloginReq extends CommonRequest {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required=true,value = "微信用户code")
	private String code;

	@ApiModelProperty("用戶信息")
	private String wxUserInfoStr;


	@ApiModelProperty("微信登录返回的encryptedData")
	private String encryptedData;

	@ApiModelProperty("ase加密算法的初始向量")
	private String iv;

	@ApiModelProperty("分享注册类型")
	private Integer recommendType;

	@ApiModelProperty("推荐用户ID")
	private String recomendUserId;

}

