package com.ltu.config.shiro.dto;

import java.util.List;

import com.ltu.model.request.CommonRequest;
import com.ltu.util.common.IdsStrToListUtils;

import freemarker.template.utility.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class UserDto extends CommonRequest {
	/*当小程序登录时username=ID password=token*/
	private Integer id;//账号主键，后台登录时为account的主键，小程序登录时为user的主键
	private String loginAccount;//登录账号 后台用户登录时为accountloinname 前端登录为mobilephone/openid
	private String userName;//账号名称/ 昵称
	private String password;//登录密码 前端仅微信公众号授权时 为随机生成的密码
	private Integer roleId;//所属角色
	private String roleName;//所属角色名称
	private Integer enable;//账号状态
	/* 登录类型：1=普通用户，2=内部人员,默认1 */
	private Integer loginType ;
	private String jwtUserKey = "";
	public void setJwtUserKey(){
		StringBuilder tokenKey = new StringBuilder();
		tokenKey.append(this.id);
		tokenKey.append("_");
		tokenKey.append(this.loginAccount);
		tokenKey.append("_");
		tokenKey.append(this.roleId);
		tokenKey.append("_");
		tokenKey.append(this.password);
		tokenKey.append("_");
		tokenKey.append(this.loginType);

		this.jwtUserKey = tokenKey.toString();
	}
	public void setJwtUserKey(String jwtUserKey){
		this.jwtUserKey=jwtUserKey;
	}
	public static UserDto getUserDtoByStr(String tokenKey){
		String[]  obj = StringUtil.split(tokenKey, '_');
		UserDto u = new UserDto();
		u.setId(Integer.valueOf(obj[0]));
		u.setLoginAccount(obj[1]);
		u.setRoleId(Integer.valueOf(obj[2]));
		u.setPassword(obj[3]);
		u.setLoginType(Integer.parseInt( obj[4]));
		return u;


	}
	
}
