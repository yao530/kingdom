package com.ltu.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMqConfig {
	@Bean
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
		connectionFactory.setPublisherConfirms(true); // 设置使用完整的回调信息确认
		connectionFactory.setPublisherReturns(true); // 设置从服务中心返回完整的消息
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		// 消息发送后强制回调标识
		rabbitTemplate.setMandatory(true);
		// 打印 发送成功的消息
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log
				.info("消息发送成功:correlationData[{}],ack[{}],cause[{}]", correlationData, ack, cause));
		// 打印 丢失的消息
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info(
				"消息丢失:exchange[{}],route[{}],replyCode[{}],replyText[{}],message:{}", exchange, routingKey, replyCode,
				replyText, message));
		return rabbitTemplate;
	}


}
