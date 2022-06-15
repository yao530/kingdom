package com.ltu.config.shiro.config;

import java.util.*;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import com.ltu.config.shiro.manager.StatelessSessionManager;
import com.ltu.constant.AccountType;
import com.ltu.constant.CommonConstant;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.filter.AnyRolesAuthorizationFilter;
import com.ltu.config.shiro.filter.JwtAuthFilter;
import com.ltu.config.shiro.realm.DbShiroRealm;
import com.ltu.config.shiro.realm.JWTShiroRealm;

@Configuration
public class ShiroConfig {
 	@Bean(name="securityManager")
    public SecurityManager securityManager(ShiroUserService shiroUserService) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //注入缓存管理器
        securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;
        List<Realm> realms=new ArrayList<Realm>();
        realms.add(jwtShiroRealm(shiroUserService));
        realms.add(dbShiroRealm(shiroUserService));
        securityManager.setRealms(realms);
        securityManager.setAuthenticator(authenticator(shiroUserService));
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(true);//打开shiro session
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
		securityManager.setSessionManager(sessionManager());
        return securityManager;
        
    }
	
	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager,ShiroUserService userService)throws Exception{
		FilterRegistrationBean<Filter> filterRegistration=new FilterRegistrationBean<Filter>();
		filterRegistration.setFilter((Filter)shiroFilter(securityManager,userService).getObject());
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setAsyncSupported(true);
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}
	
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,ShiroUserService shiroUserService){
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		//写入自实现的拦截验证
		Map<String,Filter>  filterMap = factoryBean.getFilters();
		filterMap.put("authcToken", createAuthFilter(shiroUserService));//鉴权拦截器 自己创建拦截器并实现验证
//		filterMap.put("anyRole",  createRolesFilter());//角色验证拦截器 、、 自己创建拦截器并实现角色验证
		factoryBean.setFilters(filterMap);
		factoryBean.setUnauthorizedUrl("/info403");//roles的不匹配会跳转到此请求
		factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
		return factoryBean;
	}
	
	@Bean
	protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		chainDefinition.addPathDefinition("/v2/api-docs", "anon");
		chainDefinition.addPathDefinition("/webjars/**", "anon");
		chainDefinition.addPathDefinition("/static/**", "anon");
		chainDefinition.addPathDefinition("/images/**", "anon");
		chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
		chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
		chainDefinition.addPathDefinition("/doc.html", "anon");
		chainDefinition.addPathDefinition("/index.html", "anon");
		chainDefinition.addPathDefinition("/webjars/springfox-swagger-ui/**", "anon");
		chainDefinition.addPathDefinition("/configuration/ui/**", "anon");
		chainDefinition.addPathDefinition("/configuration/security/**", "anon");
		chainDefinition.addPathDefinition("/401", "anon");
		chainDefinition.addPathDefinition("/image/**", "anon");
		chainDefinition.addPathDefinition("/doc.html/**", "anon");
		chainDefinition.addPathDefinition("/kaptcha/**", "anon");
		chainDefinition.addPathDefinition("/headPhoto/**", "anon"); //图片访问路径

		//登录注册
		chainDefinition.addPathDefinition("/login/login", "anon");
		chainDefinition.addPathDefinition("/login/forgotPassword", "anon");
		chainDefinition.addPathDefinition("/login/loginBySmsCode", "anon");
        chainDefinition.addPathDefinition("/login/loginByWx", "anon");
        chainDefinition.addPathDefinition("/login/loginByAccount", "anon");
        chainDefinition.addPathDefinition("/login/getwxAuthUrl", "anon");
        chainDefinition.addPathDefinition("/login/loginByWx", "anon");

		chainDefinition.addPathDefinition("/login/register", "anon");
		chainDefinition.addPathDefinition("/device/upReportData", "anon");
		chainDefinition.addPathDefinition("/device/online", "anon");
		chainDefinition.addPathDefinition("/payRecord/wxNotify", "anon")
		;


		chainDefinition.addPathDefinition("/bannerEntity/getList", "anon");
		chainDefinition.addPathDefinition("/artCollection/getStoryArtCollections", "anon");
		chainDefinition.addPathDefinition("/artCollection/getDetail", "anon");
		chainDefinition.addPathDefinition("/artCollection/getPageList", "anon");
		chainDefinition.addPathDefinition("/story/getMePage", "anon");
		chainDefinition.addPathDefinition("/story/getList", "anon");
		chainDefinition.addPathDefinition("/story/getDetail", "anon");
		chainDefinition.addPathDefinition("/storyTales/getDetail", "anon");
		chainDefinition.addPathDefinition("/virtualCharacter/getCreators", "anon");
		chainDefinition.addPathDefinition("/virtualCharacter/getDetail", "anon");

		chainDefinition.addPathDefinition("/imageUpload/**", "anon");

		//验证码
		chainDefinition.addPathDefinition("/admin/kaptcha/**", "anon");
		chainDefinition.addPathDefinition("/kaptcha/**", "anon");
		chainDefinition.addPathDefinition("/verifySmsCode/**", "anon");

		//Public功能
		chainDefinition.addPathDefinition("/index/**", "anon");
		chainDefinition.addPathDefinition("/websiteInfo/**", "anon");
		chainDefinition.addPathDefinition("/banner/**", "anon");
		chainDefinition.addPathDefinition("/feedback/**", "anon");
		chainDefinition.addPathDefinition("/document/**", "anon");
		chainDefinition.addPathDefinition("/demo/deadMsg", "anon");
		chainDefinition.addPathDefinition("/file/**", "anon");
		chainDefinition.addPathDefinition("/ipfs/**", "anon");


		//做用户认证，permissive参数的作用是当token无效时也允许请求访问，不会返回鉴权未通过的错误
		chainDefinition.addPathDefinition("/user/logout", "authcToken[permissive]");
		chainDefinition.addPathDefinition("/admin/logout", "authcToken[permissive]");
		chainDefinition.addPathDefinition("/admin/account/logout", "authcToken[permissive]");

		// 表示除了上面指定的路由其他的都会被jwt拦截进行token验证
		chainDefinition.addPathDefinition("/admin/**", "authcToken,roles[" + AccountType.ACCOUNT_TYPE_SYS_STAFF + "]"); //只允许admin用户访问

		//拦截前端请求
	    chainDefinition.addPathDefinition("/**", "authcToken");

		return chainDefinition;
	}

	/**
	 * session 管理对象
	 *
	 * @return DefaultWebSessionManager
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		StatelessSessionManager sessionManager = new StatelessSessionManager();
		sessionManager.setSessionIdUrlRewritingEnabled(false);
//		sessionManager.setGlobalSessionTimeout();//设置session过期时间
		return sessionManager;
	}

	@Bean("dbRealm")
	public Realm  dbShiroRealm(ShiroUserService userService){
		DbShiroRealm  dbShiroRealm = new DbShiroRealm(userService);
		return dbShiroRealm;
	}

	@Bean("jwtRealm")
	public Realm jwtShiroRealm(ShiroUserService userService){
		   JWTShiroRealm myShiroRealm = new JWTShiroRealm(userService);
		   return myShiroRealm;
	}
	
    //注意不要加@Bean注解，不然spring会自动注册成filter
    protected JwtAuthFilter createAuthFilter(ShiroUserService userService){
        return new JwtAuthFilter(userService);
    }

	//注意不要加@Bean注解，不然spring会自动注册成filter
    protected AnyRolesAuthorizationFilter createRolesFilter(){
        return new AnyRolesAuthorizationFilter();
    }

    /**
     * 初始化Authenticator
     */
    public Authenticator authenticator(ShiroUserService shiroUserService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(jwtShiroRealm(shiroUserService), dbShiroRealm(shiroUserService)));
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
    * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
    * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合noSessionCreation的Filter来实现
    */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }
    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心；	     
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }
    /**
     * 开启shiro aop注解支持. 使用代理方式; 所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
