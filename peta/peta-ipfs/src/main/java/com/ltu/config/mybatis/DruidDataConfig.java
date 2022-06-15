package com.ltu.config.mybatis;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ltu.config.properties.DruidProperties;


//@Configuration
//@EnableTransactionManagement
//@MapperScan("com.ltu.mybatis.dao.mapping.db*")
/*
 * 已禁用此配置
 */
@Deprecated
public class DruidDataConfig {  
	@Resource
	private DruidProperties druidProperties;
	
	@Bean(name="db1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")  
    public DataSource db1() {  
        DruidDataSource druidDataSource = new DruidDataSource();  
        druidProperties.config(druidDataSource);
        return druidDataSource;  
    }
	
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory( PaginationInterceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(db1());//,druidDataSource()

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
//        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);//若要debug sql语句，可打开此处
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{ paginationInterceptor });
        sqlSessionFactory.setTypeAliasesPackage("com.ltu.mybatis.entity");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml"));
        return sqlSessionFactory.getObject();
    }


	//  配置事物管理器
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(db1());//multipleDataSource(db1(), db2())
    }

    /**
     * 注册一个StatViewServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServle2(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid2/*");
        //添加初始化参数：initParams
        /** 白名单，如果不配置或value为空，则允许所有 */
        //servletRegistrationBean.addInitParameter("allow","127.0.0.1,192.0.0.1");
        /** 黑名单，与白名单存在相同IP时，优先于白名单 */
        //servletRegistrationBean.addInitParameter("deny","192.0.0.1");
        /** 用户名 */
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        /** 密码 */
        servletRegistrationBean.addInitParameter("loginPassword","admin");
        /** 禁用页面上的“Reset All”功能 */
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 注册一个：WebStatFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter2(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        /** 过滤规则 */
        filterRegistrationBean.addUrlPatterns("/*");
        /** 忽略资源 */
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.jsp,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
        return filterRegistrationBean;
    }
}  
