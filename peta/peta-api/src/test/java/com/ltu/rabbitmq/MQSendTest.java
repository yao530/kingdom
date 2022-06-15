package com.ltu.rabbitmq;



import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.ltu.BaseApplicationTest;
import com.ltu.constant.rabbitmq.RabbitConstants;
import com.ltu.service.impl.VerifySmsCodeServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQSendTest extends BaseApplicationTest {
	
	@Resource
	private RabbitTemplate rabbitTemplate;
	

	
	@Test
	public void mqSend(){
		String message="第八章 龙灵传说";
		rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_TOPIC_EXCH, "topic.queue", message);
		log.info("消息发送已完成：[{}]",message);
	}
	
	@Test
	public void timeOutMqSend(){
		String message="超时消息";
//		rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_MODE_QUEUE, "topic.queue", message);
		
		rabbitTemplate.convertAndSend(null,RabbitConstants.TIME_OUT_QUEUE ,message);
		log.info("消息发送已完成：[{}]",message);
	}
}
