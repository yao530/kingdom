package com.ltu.config.shiro;

import com.ltu.config.shiro.dto.UserDto;
import com.ltu.config.shiro.util.JwtUtils;
import com.ltu.constant.CommonConstant;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiroUtil {
    /**
     * 从token获取登陆者
     * @return
     */
    public static UserDto getPrincipalUser() {
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto)subject.getPrincipal();//获取登录用户信息
        return user;
    }

    /**
     * 从token获取登陆者id
     * @return
     */
    public static Integer getPrincipalUserId() {
        UserDto user = ShiroUtil.getPrincipalUser();
        return user == null ? null : user.getId();
    }

    /**
     * 从token获取登陆者id
     * @return
     */
    public static String getPrincipalUserName() {
        UserDto user = ShiroUtil.getPrincipalUser();
        return user == null ? null : user.getUserName();
    }

    /**
     * 校验userId是否与登录者id符合
     * @param userId
     * @return
     */
    public static boolean isPrincipalUserIdMatch(String userId) {
        if (userId == null)
            return false;

        return userId.equals(ShiroUtil.getPrincipalUserId());
    }

    /**
     * 判断登陆者是否为管理员
     * @return
     */
    public static boolean isPrincipalUserAdmin() {
        UserDto user = ShiroUtil.getPrincipalUser();
        if (user != null && user.getRoleId() == CommonConstant.LOGIN_TYPE_BACKSTAGE)
            return true;

        return false;
    }

    /**
     * 校验登录者是否有权限进行操作，管理员一定有
     * @param objUserId
     * @return
     */
    public static boolean isPrincipalUserAuthorized(String objUserId) {
        if (ShiroUtil.isPrincipalUserAdmin())
            return true;
        if (objUserId == null)
            return false;
        return objUserId.equals(ShiroUtil.getPrincipalUserId());
    }

    /**
     * 判断登陆者是否为内部员工
     * @return
     */
    public static boolean isPrincipalUserInternal() {
        UserDto user = ShiroUtil.getPrincipalUser();
        if (user != null && user.getLoginType() == CommonConstant.USER_FRONT_TYPE_INTERNAL)
            return true;

        return false;
    }
}
