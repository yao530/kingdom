package com.ltu.model.request.platinfos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=true)
public class PlatFormReq extends BaseIdReq {


	/**
	 * 信息类型 1关于我们 2 用户注册协议 3用户隐私协议
	 */

	private Integer type;

	/**
	 * 图文内容
	 */

	private String context;

	/**
	 * 状态 0无效 1有效
	 */

	private Integer status;

	/**
	 * 备注
	 */

	private String remark;

}
