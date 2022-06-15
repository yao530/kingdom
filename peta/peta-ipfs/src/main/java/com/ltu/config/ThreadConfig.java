package com.ltu.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**  
 * @Description: 配置线程池
 * @author: 若尘  
 * @date 2018年10月2日 下午3:25:30
 * @version V1.0  
 */

@Configuration
@EnableAsync
public class ThreadConfig {//
	
	
	@Bean("asyncServiceExecutor")
	public TaskExecutor   asyncExecutor(){
		ThreadPoolTaskExecutor  esecutors=new ThreadPoolTaskExecutor();
		esecutors.setCorePoolSize(5);  //设置最少维护的线程数
		esecutors.setMaxPoolSize(15);   //最多同时存在多少个线程
		esecutors.setQueueCapacity(5000); // 缓冲队列里的线程数
		esecutors.setKeepAliveSeconds(60);//设置线程空闲时间
		esecutors.setThreadNamePrefix("Customer-async-");
		esecutors.initialize();
		return esecutors;
	}
	
}
