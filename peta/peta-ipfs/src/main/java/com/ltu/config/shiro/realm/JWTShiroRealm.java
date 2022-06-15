package com.ltu.config.shiro.realm;

import com.ltu.util.common.StrUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONObject;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.dto.JWTToken;
import com.ltu.config.shiro.dto.MyUsernamepwdToken;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.config.shiro.matcher.JWTCredentialsMatcher;
import com.ltu.config.shiro.util.JwtUtils;

/**  
 * @Description: 
 * @author: 若尘  
 * @date 2019年9月16日 下午4:50:57
 * @version V1.0  
 */
public class JWTShiroRealm extends AuthorizingRealm  {
	ShiroUserService userService;
	public JWTShiroRealm() {
		// Auto-generated constructor stub
	}

	/**
	 * @param userService
	 */
	public JWTShiroRealm(ShiroUserService userService) {
		// Auto-generated constructor stub
		this.userService = userService;
		this.setCredentialsMatcher(new JWTCredentialsMatcher());
	}

	@Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken; 
    }


	/**
	 * 角色认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
		UserDto user = (UserDto)principals.getPrimaryPrincipal();
//		if(user.getRoles() == null){
//			user.setRoles(userService.getAccountRoles(user.getId(), user.getAccountType()));
//		}
//		if(user.getRoles() != null){
//			simpleAuthenticationInfo.addRoles(user.getRoles());
//		}
		return simpleAuthenticationInfo;
	}

	 /**
     * 访问api接口的安全认证 x-auth-token
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。2_15011678983__1_1_3
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		JWTToken jwtToken = (JWTToken)authcToken;
		
		String salt = userService.getSalt(jwtToken.getUser().getJwtUserKey());// 需要过期用这个
//		String salt = userService.getSalt();
		
		if(StrUtils.isTrimNull(salt)){
			 throw new AuthenticationException("认证失败！请重新登录");
		}
		return new SimpleAuthenticationInfo(jwtToken.getUser(),salt,"jwtRealm");
	}
}
