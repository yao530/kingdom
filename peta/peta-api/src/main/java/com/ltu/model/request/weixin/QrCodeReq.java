package com.ltu.model.request.weixin;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class QrCodeReq extends CommonRequest {


	@NotBlank(message = "二维码参数的主键")
	private String sceneId;

	@ApiModelProperty(value = "二维码业务场景 1、艺术家白名单，2、网红宠物白名单，3、藏品转赠，4、定投藏品，")
	private Integer codeType;

	@ApiModelProperty(value = "临时二维码过期时间")
	private Long expireTime=7200l;
}
