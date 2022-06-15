package com.ltu.aop;

import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.OperateLogEntity;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.AccountService;
import com.ltu.service.OperateLogService;

import com.ltu.util.common.IpUtils;
import com.ltu.util.common.StrUtils;
import com.ltu.util.redis.RedistUtil;
import com.ltu.util.security.Md5Util;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * @author lyz
 * @title: OperationAspect
 * @projectName springcloud
 * @date 2020/9/23
 * @description: 操作日志切面处理类
 */
@Aspect
@Component
public class OperationLogAspect {

	@Autowired
	OperateLogService logDao;

	@Autowired
    RedistUtil redistUtil;

	@Autowired
	AccountService accountService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 设置操作日志切入点   在注解的位置切入代码
	 */
	@Pointcut("@annotation(com.ltu.aop.OperationLogAnnotation)")
	public void operLogPoinCut() {
	}


	@AfterReturning(returning  /**
	 * 记录操作日志
	 * @param joinPoint 方法的执行点
	 * @param result  方法返回值
	 * @throws Throwable
	 */ = "result", value = "operLogPoinCut()")
	public void saveOperLog(JoinPoint joinPoint, Object result) throws Throwable {
		// 获取RequestAttributes
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		// 从获取RequestAttributes中获取HttpServletRequest的信息
		HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		try {
			//将返回值转换成map集合
			//Map<String, String> map = (Map<String, String>) result;
			CodeDataResp codeDataResp = (CodeDataResp) result;

			OperateLogEntity operationLog = new OperateLogEntity();
			// 从切面织入点处通过反射机制获取织入点处的方法
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			//获取切入点所在的方法
			Method method = signature.getMethod();
			UserDto userDto = ShiroUtil.getPrincipalUser();
			AccountEntity accountEntity = accountService.getById(userDto.getId());
			//获取操作
			OperationLogAnnotation annotation = method.getAnnotation(OperationLogAnnotation.class);
			if (accountEntity!=null&&annotation != null) {
				String mdUrl = Md5Util.encoderByMd5(request.getRequestURL().toString());
//				String url= (String) redistUtil.getValue(IpUtils.getIp(request)+":"+mdUrl);
//				if (StrUtils.isTrimNull(url)){

					//redistUtil.setValue(IpUtils.getIp(request)+":"+mdUrl,mdUrl,1*60*60l);//1小时内避免多次记录
					operationLog.setModule(annotation.operModul());
					operationLog.setMethods(method.getName());
					operationLog.setContent(annotation.operDesc());

					//操作时间
					operationLog.setLogDate(codeDataResp.getTimestamp());

					//操作用户
					operationLog.setUserName(accountEntity.getAccountName());
					operationLog.setRoleName(accountEntity.getRoleName());
					operationLog.setOperaterId(userDto.getId());
					operationLog.setAccountType(userDto.getRoleId());
					operationLog.setRoleName(userDto.getRoleName());
					//操作IP
					operationLog.setIp(IpUtils.getIp(request));
					//返回值信息
					operationLog.setActionUrl(request.getRequestURL().toString());

					Map<String,String[]> map= request.getParameterMap();



					//保存日志
					operationLog.setCommite(codeDataResp.getCode());
					logDao.save(operationLog);
				}

			//}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

