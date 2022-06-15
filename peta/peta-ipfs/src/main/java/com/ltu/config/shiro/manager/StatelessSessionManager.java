package com.ltu.config.shiro.manager;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义SessionManager，兼容小程序不支持cookie的情况
 */
public class StatelessSessionManager extends DefaultWebSessionManager {

    public final static String SHIRO_SESSION_UUID_NAME = "x-session-id";
    public final static String SHIRO_SESSION_ID_SOURCE = "request_header";

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sessionId = WebUtils.toHttp(request).getHeader(SHIRO_SESSION_UUID_NAME); //这个修改就是从请求头里来获取
        if (StringUtils.isEmpty(sessionId)) {
            //如果没有携带uuid参数则按照父类的方式在cookie进行获取
            return super.getSessionId(request, response);
        } else {
            //如果请求头中有uuid则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, SHIRO_SESSION_ID_SOURCE); //session_id  来源
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
    }
}

