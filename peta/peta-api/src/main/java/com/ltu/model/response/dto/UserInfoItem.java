package com.ltu.model.response.dto;


   
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoItem {


	@ApiModelProperty(value = " user_nick:用户昵称 ")
	private String userNick;
	@ApiModelProperty(value = " user_avatar:logo ")
	private String userAvatar;

	private String token;

}
