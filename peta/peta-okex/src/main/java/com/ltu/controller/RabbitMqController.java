package com.ltu.controller;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ltu.constant.rabbitmq.RabbitConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


@Api(tags="消息模块")
@RestController
@RequestMapping("/rabbit")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RabbitMqController {

	@Resource
	private RabbitTemplate rabbitTemplate;
	
	@ApiOperation(value="测试Topic订阅模式")
	@RequestMapping(value="/topicMsg",method=RequestMethod.GET)
	public String deadMsg() {
		String message="主题okex消息";
		String message2="超时消息";
		
		String message3="okex生产的超时消息";
		
//		RabbitConstants.FANOUT_DEAD_QUEUE2
		rabbitTemplate.convertAndSend(null, RabbitConstants.TIME_OUT_QUEUE2, message3);
		rabbitTemplate.convertAndSend(null, RabbitConstants.TIME_OUT_QUEUE, message2);
		//消息发送后在pata-api模块下的com.ltu.listenre包下由RabbitMQHandler负责处理生产的消息
		rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_OKEX_EXCH, RabbitConstants.TOPIC_OKEX_QUEUE, message);
		return "消息发送成功";
	}
}
