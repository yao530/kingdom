package com.ltu.config.shiro.realm;

import com.ltu.base.State;
import com.ltu.constant.CommonConstant;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.dto.MyUsernamepwdToken;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.util.exception.ApiException;

/**  
 * @Description: 
 * @author: 若尘  
 * @date 2019年9月16日 下午5:16:20
 * @version V1.0  
 */
public class DbShiroRealm extends AuthorizingRealm {

	private ShiroUserService shiroUserService;
	public DbShiroRealm() {
		// Auto-generated constructor stub
	}
	/**
	 * @param shiroUserService
	 */
	public DbShiroRealm(ShiroUserService shiroUserService) {
		// Auto-generated constructor stub
		this.shiroUserService = shiroUserService;
//		this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
	}
	
	@Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

	/*
	 * (non-Javadoc)
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		MyUsernamepwdToken userToken = (MyUsernamepwdToken) token;
		UserDto user;
		if(userToken.getLoginType() == CommonConstant.LOGIN_TYPE_BACKSTAGE){
			user = shiroUserService.getAccountInfo(userToken.getUsername());
			if(user == null){
				throw new AuthenticationException("用户不存在");
			}
			if(user.getEnable() == 0){
				throw new ApiException(State.USERBAN);
			}
		}else{
			user = shiroUserService.getUserInfo(userToken.getUsername());
			if(user == null){
				throw new AuthenticationException("用户未登录");
			}
		}
		return new SimpleAuthenticationInfo(user, userToken.getPassword(), "dbRealm");

	}

	
	/* (non-Javadoc)
	 * 获取用的角色
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
		UserDto user = (UserDto)principals.getPrimaryPrincipal();
//		if(user.getRoles() == null){
//			user.setRoles(shiroUserService.getAccountRoles(user.getId(), user.getAccountType()));
//		}
//		if(user.getRoles() != null){
//			simpleAuthenticationInfo.addRoles(user.getRoles());
//		}
		return simpleAuthenticationInfo;
	}
}
