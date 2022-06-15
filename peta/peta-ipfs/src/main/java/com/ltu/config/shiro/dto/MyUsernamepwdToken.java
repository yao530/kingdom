package com.ltu.config.shiro.dto;

import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Data;

/**  
 * @Description: 
 * @author: 若尘  
 * @date 2019年9月27日 下午5:03:26
 * @version V1.0  
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class MyUsernamepwdToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 1L;
	private Integer loginType;//2-后台，1-前端
	private Integer roleId;//
	public MyUsernamepwdToken(){}
	public MyUsernamepwdToken(String username,String pwd,int loginType,int roleId){
		super(username,pwd);
		this.loginType = loginType;
		this.roleId = roleId;
	}

}
