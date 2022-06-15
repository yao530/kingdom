package com.ltu.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**  
 * @Description: 配置开启分页
 * @author: 若尘  
 * @date 2019年9月23日 下午4:27:24
 * @version V1.0  
 */
@MapperScan(basePackages={"com.ltu.mapper"})
@Configuration
public class MybatisPlusConfig {
	
	@Bean
	public PaginationInterceptor paginationInterceptor(){
	    PaginationInterceptor page = new PaginationInterceptor();
	    return page;
	}

}
