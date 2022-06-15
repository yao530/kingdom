package com.ltu.config.shiro;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.MyUsernamepwdToken;
import com.ltu.constant.AccountType;
import com.ltu.constant.CommonConstant;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.service.AccountService;
import com.ltu.service.RoleService;
import com.ltu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ltu.config.shiro.dto.UserDto;
import com.ltu.config.shiro.util.JwtUtils;


import com.ltu.util.redis.RedistUtil;

/**  
 * @Description: shiro业务的交互代码
 * @author: 若尘  
 * @date 2019年9月18日 下午12:01:51
 * @version V1.0  
 */
@Component
public class ShiroUserService {
	@Autowired RedistUtil redisUtil;
	@Autowired AccountService accountDaoImpl;
	@Autowired RoleService roleDaoImpl;
	@Autowired UserService userService;
	private final static String TEMP_SALT="d43096b36c73df326d2ded9ccdd3cf00";//不缓存盐值，保证token永久有效
	/**
	 * 用户登陆
	 * @param userLoginReq
	 * @param loginType:1普通用户，2后台用户
	 * @return
	 */
	public String userLoginGetToken(LoginReq userLoginReq, Integer loginType, Integer userType) {
		MyUsernamepwdToken token = new MyUsernamepwdToken(userLoginReq.getUsername(),
				userLoginReq.getPassword(),
				loginType,
				userType);
		Subject subject = SecurityUtils.getSubject();
		subject.login(token); //登录验证交给shiro的realm  
		UserDto user = (UserDto) subject.getPrincipal();//登录成功获取用户信息
		System.out.println("登录者信息："+user.toString());
		return generateJwtToken(user.getJwtUserKey());
//		return generateJwtToken2(user.getJwtUserKey()); //不需要过期用这个
	}

	/**
	 * 注销登录
	 */
	public void userLogout() {
		UserDto user = ShiroUtil.getPrincipalUser();
		Subject subject = SecurityUtils.getSubject();
		if(subject.getPrincipals() != null){
			
			SecurityUtils.getSubject().logout();
		}
		
		//清除 JWT的token的salt
		removeShiroUser(user.getJwtUserKey());
	}

	/**
	 * @date 2019年9月18日
	 * @author 若尘
	 * @Description 生成JWTtoken，要写到Header里面去的
	 * @param
	 * @return
	 * String
	 */
	public String generateJwtToken(String jwtUserKey) {
		String  salt = "";
		String key = "token_salt_key:" + jwtUserKey;
		if(redisUtil.containsKey(key)){
			salt = (String)redisUtil.getValue(key);
		}else{
			salt = JwtUtils.generateSalt();
			redisUtil.setValue(key, salt, CommonConstant.DEFAULT_LOGIN_EXPIRE_TIME);
		}
		String newToken = JwtUtils.sign(jwtUserKey, salt, CommonConstant.DEFAULT_LOGIN_EXPIRE_TIME);
		return  newToken;
	}
	/**
	 * 特殊需求，盐值不过期，tokey永久有效，正常情况使用 generateJwtToken 方法 2021-12-09
	 * @param jwtUserKey
	 * @return
	 */
	public String generateJwtToken2(String jwtUserKey) {
		String newToken = JwtUtils.sign(jwtUserKey, TEMP_SALT, CommonConstant.DEFAULT_LOGIN_EXPIRE_TIME);
		return  newToken;
	}

	/**
	 * 注销登录
	 * @param jwtUserKey
	 */
	public void removeShiroUser(String jwtUserKey){
		String key = "token_salt_key:" + jwtUserKey;
		redisUtil.removeKey(key);
	}

	/**
	 * @date 2019年9月17日
	 * @author 若尘
	 * @Description 从数据库查出当前商户
	 * @param username
	 * @return
	 * UserDto
	 */
	public UserDto getAccountInfo(String username) {
		//这里的代码要替换成从数据库里面查找该用户信息
		QueryWrapper<AccountEntity> condition=new QueryWrapper<>();
		condition.eq("account_login_name",username);
		AccountEntity account = accountDaoImpl.getOne(condition);
		return getUserDtoByAccount(account);
	}


	/**
	 * 获取管理员用户数据
	 * @param account
	 * @return
	 */
	public UserDto getUserDtoByAccount(AccountEntity account){
		if(account == null) return null;
		UserDto user = new UserDto();
		user.setPassword(account.getPassword());
		user.setId(account.getId());
		user.setLoginAccount(account.getAccountLoginName());
		user.setEnable(account.getStatus());
		user.setRoleName(account.getRoleName());
		user.setRoleId(account.getRoleId());
		user.setLoginType(CommonConstant.LOGIN_TYPE_BACKSTAGE);
		user.setJwtUserKey();
		return user;
	}




	/**
	 * 获取加盐值
	 * @param username
	 * @return
	 */
	public String getSalt(String username){
		String key="token_salt_key:"+ username;
		if(!redisUtil.containsKey(key)){ return null;}		
		return (String) redisUtil.getValue(key);
	}
	/**
	 * 获取加盐值
	 * @param
	 * @return
	 */
	public String getSalt(){
		return TEMP_SALT;
	}

	/**
	 * 获取用户权限列表
	 * @param userId
	 * @return
	 */
	public  List<String> getAccountRoles(Integer userId, Integer type){
		List<String> roles = new ArrayList<String> ();
		//设置为管理页面使用权限
		if (type == 2) {
			roles.add(AccountType.ACCOUNT_TYPE_SYS_STAFF);
		}
		return roles;
	}

	/**
	 * 获取前端用户数据
	 * @param
	 * @return
	 */
	public UserDto getUserInfo(String userLoginAccount) {
		return getUserDto(userService.getUser(userLoginAccount));
	}

	public UserDto getUserInfoByUserId(String userId) {
		QueryWrapper<UserEntity> condition = new QueryWrapper<>();
		condition.eq("id",userId);
		return getUserDto(userService.getOne(condition));
	}

	private UserDto getUserDto(UserEntity userEntity) {
		UserDto user = new UserDto();

		user.setId(userEntity.getId());
		user.setUserName(userEntity.getUserNick());
		user.setLoginAccount(userEntity.getUserLoginAccount());
		user.setPassword(userEntity.getPassword());
		user.setRoleId(userEntity.getRoleId());
		user.setRoleName(userEntity.getRoleName());
		user.setEnable(userEntity.getStatus());
		user.setLoginType(CommonConstant.LOGIN_TYPE_FRONT);
		user.setJwtUserKey();

		return user;

	}
}
