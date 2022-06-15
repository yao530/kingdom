package com.ltu;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ltu.util.IPUtil;

import io.netty.channel.ChannelFuture;

@Slf4j
//@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@EnableAsync
//自动填充或更新实体中的CreateDate、CreatedBy
@SpringBootApplication
public class EntryApplication extends SpringBootServletInitializer implements CommandLineRunner{
	   
	    @Value("${netty-port}")
	    private Integer port;

	public static void main(String[] args) {
		ConfigurableApplicationContext cfa=SpringApplication.run(EntryApplication.class, args);
		Environment env = cfa.getEnvironment();
		String portStr = env.getProperty("server.port");
		String pathStr = env.getProperty("server.servlet.context-path");
		System.out.println("\n----------------------------------------------------------\n\t"
				+ "Application '{"
				+ env.getProperty("spring.application.name")
				+ "}' is running! Access URLs:\n\t"
				+ "API: \t\thttp://localhost:"
				+ portStr + pathStr
				+ "\n----------------------------------------------------------");
		log.info("启动成功,接口文档访问路径=http://localhost:" + portStr + pathStr + "doc.html");
		log.info("启动成功,接口文档访问路径=http://"+IPUtil.getInnerIp()+":" + portStr + pathStr + "doc.html");
//		log.info("启动成功,接口文档访问路径=http://"+IPUtil.getOuterIp()+":" + portStr + pathStr + "doc.html");


	}
    /**
     * 此处启动netty服务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
//    	log.info("TCP服务端口：{}",port);
//        InetSocketAddress address = new InetSocketAddress(port);
//        // 启动netty服务器
//        ChannelFuture channelFuture = nettyServer.startServer(address);
//        // 钩子方法，关闭服务器
//        Runtime.getRuntime().addShutdownHook(new Thread(() ->
//                nettyServer.closeServer()
//        ));
//        channelFuture.channel().closeFuture().sync();
    }

}
