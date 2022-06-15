package com.ltu.config.shiro.dto;

import javax.validation.constraints.NotNull;

import org.apache.shiro.authc.HostAuthenticationToken;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JWTToken implements HostAuthenticationToken {
	private static final long serialVersionUID = 9217639903967592166L;
	private UserDto user;
	private String username;
	private String password;
	private String token;
    private String host;
    public JWTToken(String token,UserDto user){
		this.token=token;
		this.user=user;
		this.username=user.getLoginAccount();
		this.password=user.getPassword();
				
	};
    public JWTToken(String token,String host) {
    	 this.token = token;
    	 this.host=host;
    }
	
	@Override
	public Object getPrincipal() {
		return token;
	}
	
	@Override
	public Object getCredentials() {
		return  token;
	}
}
