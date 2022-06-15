package com.ltu.rabbitmq;

import javax.annotation.Resource;

import org.junit.Test;

import com.ltu.BaseApplicationTest;
import com.ltu.service.impl.VerifySmsCodeServiceImpl;

public class SendSMS  {
	
	
	
	public  static void main(String[] args) {
		VerifySmsCodeServiceImpl.sendSmsTest("15011678982", "123456");
		
	}

}
