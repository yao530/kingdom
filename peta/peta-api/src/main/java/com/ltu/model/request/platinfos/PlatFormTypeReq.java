package com.ltu.model.request.platinfos;

import com.ltu.model.request.CommonRequest;
import com.ltu.model.request.base.BaseIdReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class PlatFormTypeReq extends CommonRequest {


	/**
	 * 信息类型 1关于我们 2 用户注册协议 3用户隐私协议
	 */

	private Integer type;


}
