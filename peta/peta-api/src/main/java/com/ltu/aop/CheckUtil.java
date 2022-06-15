package com.ltu.aop;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ltu.base.GenericEntity;



/**
 *参数模板的参数验证
 */
public class CheckUtil {
	
	//调用JSR303验证工具，校验参数
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	//private final static Logger logger = LoggerFactory.getLogger(CheckUtil.class);
	
	public static Map<String,String>  check(GenericEntity entity) {
		if (factory == null) {
			factory = Validation.buildDefaultValidatorFactory();
		}
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<GenericEntity>> constraintViolations = validator.validate(entity);
		Map<String,String> messageMap=new HashMap<>();
		for (ConstraintViolation<GenericEntity> constraintViolation : constraintViolations) {
			/*System.out.println("对象属性:"+constraintViolation.getPropertyPath());  
            System.out.println("国际化key:"+constraintViolation.getMessageTemplate()); */
			String message = constraintViolation.getMessage();
			messageMap.put(constraintViolation.getPropertyPath().toString(), message);
			//logger.info(message);
		}
		return messageMap;
	}

}
