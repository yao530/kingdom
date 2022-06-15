package com.ltu.config.shiro.matcher;

import java.io.UnsupportedEncodingException;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ltu.config.shiro.dto.JWTToken;
import com.ltu.config.shiro.dto.MyUsernamepwdToken;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.config.shiro.filter.JwtAuthFilter;
import com.ltu.config.shiro.util.JwtUtils;

/**  
 * @Description: 验证请求中的jwt的token核心验证代码
 * @author: 若尘  
 * @date 2019年9月18日 下午5:28:30
 * @version V1.0  
 */
public class JWTCredentialsMatcher implements CredentialsMatcher {
	private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
	/**
	 * jwt token验证
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		JWTToken myToken=(JWTToken) token;
		String jwtToken=myToken.getToken();
		String salt = info.getCredentials().toString();
		try {
			Algorithm algorithm=Algorithm.HMAC256(salt);
			JWTVerifier verifier= JWT.require(algorithm).withClaim("username", myToken.getUser().getJwtUserKey()).build();
			verifier.verify(jwtToken);
			return true;
		} catch (IllegalArgumentException |UnsupportedEncodingException | JWTVerificationException e) {
			log.error("JWT Token Error :{}",e.getMessage());
		} 
		return false;
	}

}
