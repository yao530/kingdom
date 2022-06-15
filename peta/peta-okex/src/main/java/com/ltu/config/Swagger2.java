package com.ltu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@Profile("dev")
@EnableSwagger2
@EnableKnife4j
public class Swagger2 implements WebMvcConfigurer {

	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
				.groupName("鱼塘小助手API")
                .select()//ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                .apis(RequestHandlerSelectors.basePackage("com.ltu.controller"))
                .paths(PathSelectors.any())
                .build();
    }

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("鱼塘小助手API")
				.description("1、登陆成功后接口会返回token，请加在header的x-auth-token字段上；系统会通过token获取userId；</br>" +
						"4、请求返回状态值，status=200是成功，其他状态值：500内部错误、401登陆过期、404参数错误、403无操作权限、602用户被禁用</br>" +
						"5、所有涉及图片链接的字段，统一使用完整链接保存</br>" )
				.contact(new Contact("李生","",""))
				.version("1.0")
				.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("index.html").addResourceLocations("classpath:/META-INF/resources/static/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("index.html").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/images/");
	}
}
