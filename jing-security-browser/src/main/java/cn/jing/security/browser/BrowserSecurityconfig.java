/**
 * 
 */
package cn.jing.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import cn.jing.security.core.authentication.FormAuthenticationConfig;
import cn.jing.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import cn.jing.security.core.authorize.AuthorizeConfigManager;
import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * function:浏览器环境下的安全配置主类
 * 
 * WebSecurityConfigurerAdapter是默认情况下springsecurity的http配置，通过重载configure(WebSecurity)方法来配置
 * SpringSecurity的Filter链（WEB安全），通过重载configure(HttpSecurity)方法来配置如何通过过滤器保护请求（HTTP请求安全处理），
 * 通过重载configure(AuthenticationManagerBuilder)方法来配置user-detail服务（身份验证管理生成器）。
 * 
 * @author liangjing
 *
 */
@Configuration
public class BrowserSecurityconfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	// 数据库的相关配置在demo模块中的application.properties里
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private SpringSocialConfigurer jingSocialSecurityConfig;

	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;

	/**
	 * function：配置了这个Bean以后，从前端传递过来的密码将被加密
	 * 
	 * @return PasswordEncoder实现类对象
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * function:"记住我功能"的token存取器配置
	 * 
	 * @return
	 */
	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		// 指定数据源
		tokenRepository.setDataSource(dataSource);
		// tokenRepository.setCreateTableOnStartup(true); 建表
		return tokenRepository;
	}

	/**
	 * function:配置如何通过过滤器保护请求（HTTP请求安全处理）
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		formAuthenticationConfig.configure(http);

		http.apply(validateCodeSecurityConfig).and().apply(smsCodeAuthenticationSecurityConfig).and()
				.apply(jingSocialSecurityConfig).and()
				// "记住我功能"配置，如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
				.rememberMe().tokenRepository(tokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService).and()
				// session管理相关配置
				.sessionManagement().invalidSessionStrategy(invalidSessionStrategy) // session失效时，默认的处理策略
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions()) // 表示最大的Session数量，如果设成1的话，那么同一个用户后边登录所产生的session就会把之前登录所产生的session给失效掉
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin()) // 达到最大session时是否阻止新的登录请求
				.expiredSessionStrategy(sessionInformationExpiredStrategy).and().and() // 并发登录导致session失效时，默认的处理策略
				// 用户退出成功相关配置
				.logout().logoutUrl("/signOut").logoutSuccessHandler(logoutSuccessHandler).deleteCookies("JSESSIONID")
				.and()
				// 防止跨域攻击
				.csrf().disable();

		authorizeConfigManager.config(http.authorizeRequests());
	}

}
