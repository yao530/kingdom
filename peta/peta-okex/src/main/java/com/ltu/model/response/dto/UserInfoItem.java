package com.ltu.model.response.dto;


   
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//@Data
public class UserInfoItem {
	 @ApiModelProperty(value = " 用户ID")
	  private String id;
	 @ApiModelProperty(value = " nick_name:用户昵称 ")
	  private String nickName;

			 @ApiModelProperty(value = " login_token:登陆令牌 ")
	  private String token;

			 @ApiModelProperty(value = " head_photo:头像 ")
	  private String headPhoto="";
			public String getNickName() {
				return nickName;
			}
			public void setNickName(String nickName) {
				this.nickName = nickName;
			}

			public String getHeadPhoto() {
				return headPhoto;
			}
			public void setHeadPhoto(String headPhoto) {
				this.headPhoto = headPhoto;
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
		
		
			 
		 
		
}
