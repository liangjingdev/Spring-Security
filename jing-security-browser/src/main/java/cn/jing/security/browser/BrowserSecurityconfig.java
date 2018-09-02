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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import cn.jing.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.validate.code.ValidateCodeFilter;

/**
 * function:浏览器环境下安全配置主类
 * 
 * @author liangjing
 *
 */
@Configuration
public class BrowserSecurityconfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private AuthenticationSuccessHandler jingAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler jingAuthenticationFailureHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	// 数据库的相关配置在demo模块中的application.properties里
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeFilter validateCodeFilter;

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
	 * function：记住我功能的token存取器配置
	 * 
	 * @return
	 */
	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		// 指定数据源
		tokenRepository.setDataSource(dataSource);
		// tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class).formLogin()
				.loginPage("/authentication/require").loginProcessingUrl("/authentication/form")
				.successHandler(jingAuthenticationSuccessHandler).failureHandler(jingAuthenticationFailureHandler).and()
				// ----记住我功能配置---
				.rememberMe().tokenRepository(tokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService)
				// ----记住我功能配置---
				.and().authorizeRequests()
				.antMatchers("/authentication/require", securityProperties.getBrowser().getLoginPage(), "/code/*")
				.permitAll().anyRequest().authenticated().and().csrf().disable()
				.apply(smsCodeAuthenticationSecurityConfig);
	}

}
