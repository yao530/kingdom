package com.ltu.config.shiro.filter;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.dto.JWTToken;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.config.shiro.util.JwtUtils;
import com.ltu.constant.CommonConstant;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.util.exception.ApiException;


/**
 *  注入到shiroFilter的jwtFilter
 * @Description: 该拦截器内的方法执行顺序为：preHandle->isAccessAllowed ->executeLogin
 * @author: 若尘  
 * @date 2019年3月11日 下午2:13:11
 * @version V1.0
 */
public class JwtAuthFilter extends AuthenticatingFilter {
	private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    //token生存时间小于或=300秒时自动刷新
	private static final Long tokenRefreshInterval = CommonConstant.DEFAULT_TOKEN_REFRESH_TIME;
	ShiroUserService userService;
	public JwtAuthFilter() {
		// Auto-generated constructor stub
	}
	/**
	 * @param userService
	 */
	public JwtAuthFilter(ShiroUserService userService) {
		// Auto-generated constructor stub
		this.userService = userService;
	}
	
    /**
     * 对跨域提供支持
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){
       fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }
    
    /**
     *是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	 HttpServletResponse httpResponse = (HttpServletResponse) response;
         httpResponse.setCharacterEncoding("UTF-8");
         httpResponse.setContentType("application/json");
         httpResponse.setStatus(HttpStatus.SC_OK);
         fillCorsHeader(WebUtils.toHttp(request), httpResponse);
		 
        if(this.isLoginRequest(request, response))
            return true;

        Boolean afterFiltered = (Boolean)request.getAttribute("jwtShiroFilter.FILTERED");
        if(BooleanUtils.isTrue(afterFiltered))
            return true;

        boolean isLogged = false;
        try {
            isLogged = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            log.error("Not found any token");
        }
        catch (Exception e) {
            log.error("Error occurs when login", e);
        }

        boolean allowed = super.isPermissive(mappedValue);
        boolean accessable = allowed || isLogged;
        

        //此处检测出来的无权限，仅限定于登录失效；roles的不匹配在setUnauthorizedUrl处设置
        if (accessable==false) {
           
            String jsonStr = JSONObject.toJSONString(CodeDataResp.valueOfErrorNotLogin());
            try {
				httpResponse.getWriter().write(jsonStr);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
            return  false;
        }

        return accessable;
	 }
    
    /**
     *验证成功时检测token是否需要刷新
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JWTToken){
        	JWTToken jwtToken = (JWTToken)token;
            boolean shouldRefresh = shouldTokenRefresh(JwtUtils.getIssuedAt(jwtToken.getToken()));
            if(shouldRefresh) {
            	UserDto user=jwtToken.getUser();
                newToken = userService.generateJwtToken(user.getJwtUserKey());
            }
        }
        if(StringUtils.isNotBlank(newToken))
            httpResponse.setHeader("x-auth-token", newToken);
        
        return true;
    }

    /**
     * 登录失败时的操作
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
//        httpResponse.setCharacterEncoding("UTF-8");
//        httpResponse.setContentType("application/json");
//        httpResponse.setStatus(HttpStatus.SC_OK);//SC_NON_AUTHORITATIVE_INFORMATION
//        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
//        httpResponse.getWriter().write( JSONObject.toJSONString(CodeDataResp.valueOfErrorNotLogin()));
        return false;
    }
    
//    @Override
//	protected boolean  onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
//		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType("application/json");
//        httpServletResponse.setStatus(401);
//       String res= JSONObject.toJSONString(CodeDataResp.valueOfErrorNotLogin());
//        httpServletResponse.getWriter().write(res);
//        return false;
//	}
    

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
    	JWTToken token = (JWTToken)createToken(request,response);

        if(token == null){
//        	response.getWriter().write("Authentication failed");
        	return false;
        }
        System.out.println("\n 登录者信息："+token.getUser().toString());
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } 
        catch (AuthenticationException e) {
        	e.printStackTrace();
        	log.error("shiro身份认证失败");
            return onLoginFailure(token, e, request, response);
        }
    }
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
            ServletRequest request, ServletResponse response) {
    	
//	  HttpServletResponse httpServletResponse = (HttpServletResponse) response;
// 
//      try {
//    	   httpServletResponse.setCharacterEncoding("UTF-8");
//    	     httpServletResponse.setContentType("application/json");
//    	      httpServletResponse.setStatus(401);
//    	      String res= JSONObject.toJSONString(CodeDataResp.valueOfErrorNotLogin());
//		httpServletResponse.getWriter().write(res);
//	} catch (IOException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
    
    	return false;
    }

    
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("x-auth-token");
        return StringUtils.removeStart(header, "Bearer ");
    }
    
    /**
     * 是否需要刷新token
     */
    protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }
    
    /**
     * 对跨域支持的配置 -- 在WebConfig类进行配置
     */
    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
    }

	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		String jwtToken = getAuthzHeader(request);
        if(!StringUtils.isNotBlank(jwtToken)  ){// || JwtUtils.isTokenExpired(jwtToken
            return null; 
        }
        String username = JwtUtils.getUsername(jwtToken);
        UserDto user = UserDto.getUserDtoByStr(username);
        user.setJwtUserKey(username);
        JWTToken token = new JWTToken(jwtToken,user);
	    return token;
	}
}
