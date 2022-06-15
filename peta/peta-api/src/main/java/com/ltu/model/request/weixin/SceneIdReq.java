package com.ltu.model.request.weixin;

import com.ltu.model.request.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class SceneIdReq extends CommonRequest {


	private String scene_str;

	private String scene_id;
}
