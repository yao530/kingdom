package com.ltu.model.request.weixin;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class CodePostReq extends CommonRequest {


	@NotBlank(message = "该二维码有效时间，以秒为单位")
	private Long expire_seconds;

	@ApiModelProperty(value = "二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值")
	private String action_name;

	@ApiModelProperty(value = "二维码详细信息")
	private SceneReq action_info;



}
