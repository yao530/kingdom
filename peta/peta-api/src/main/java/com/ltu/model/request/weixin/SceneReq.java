package com.ltu.model.request.weixin;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class SceneReq extends CommonRequest {

	private SceneIdReq scene;

}
