package com.ltu.config.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

/**  
 * @Description: 此类没有用到，因为shiro已对角色验证提供 and or策略选择
 * @author: 若尘  
 * @date 2019年9月16日 下午5:51:14
 * @version V1.0  
 */
public class AnyRolesAuthorizationFilter extends AuthorizationFilter {

	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		 HttpServletResponse httpResponse = (HttpServletResponse) response;
         httpResponse.setCharacterEncoding("UTF-8");
         httpResponse.setContentType("application/json");
         fillCorsHeader(WebUtils.toHttp(request), httpResponse);
		
		Subject subject = getSubject(request, response);
		String[] arrRole = (String[]) mappedValue;
		if(arrRole == null || arrRole.length == 0){   //如果没有设置角色访问限制则直接通过
			return true;
		}
		for(String role : arrRole){
			if(subject.hasRole(role))
				return true;
		}
		return false;
	}
	  /**
     * 对跨域支持的配置 -- 在WebConfig类进行配置
     */
    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
    }

	/**
	 * 角色验证未通过时
	 */
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response){
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
		 fillCorsHeader(WebUtils.toHttp(request), httpResponse);
		return false;
	}
}
