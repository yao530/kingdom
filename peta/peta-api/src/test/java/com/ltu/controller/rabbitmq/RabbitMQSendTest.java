package com.ltu.controller.rabbitmq;



import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.ltu.BaseApplicationTest;
import com.ltu.constant.rabbitmq.RabbitConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitMQSendTest extends BaseApplicationTest {
	
	@Resource
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 测试订阅模式
	 */
	@Test
	public void mqSend(){
		String message="第八章 龙灵传说";
		rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_TOPIC_EXCH, "topic.queue", message);
		log.info("消息发送已完成：[{}]",message);
	}
	
	/**
	 * 测试死信消息，延迟消息模式
	 */
	@Test
	public void timeOutMqSend(){
		String message="超时消息";
//		rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_MODE_QUEUE, "topic.queue", message);
		
		rabbitTemplate.convertAndSend(null,RabbitConstants.TIME_OUT_QUEUE ,message);
		log.info("消息发送已完成：[{}]",message);
	}
}
