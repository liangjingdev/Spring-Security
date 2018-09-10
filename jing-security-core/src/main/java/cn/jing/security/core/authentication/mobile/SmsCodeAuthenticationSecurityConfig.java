/**
 * 
 */
package cn.jing.security.core.authentication.mobile;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

/**
 * function:短信登录安全配置类,把AuthenticationManager、SmsCodeAuthenticationFilter、SmsCodeAuthenticationProvider、SmsCodeAuthenticationToken这四个组件串起来，将其加入到SpringSecurity中，让其可以执行短信登录的完整的处理。
 * 
 * @author liangjing
 *
 */
@Component
public class SmsCodeAuthenticationSecurityConfig
		extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PersistentTokenRepository persistentTokenRepository;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
		// 第一步，需要配置AuthenticationManager
		smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		// 第二步，需要把过滤器与其它过滤器的成功处理器和失败处理器统一起来
		smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		// 短信登录的'记住我'功能实现
		String key = UUID.randomUUID().toString();
		smsCodeAuthenticationFilter.setRememberMeServices(
				new PersistentTokenBasedRememberMeServices(key, userDetailsService, persistentTokenRepository));
		// 第三步，需要配置UserDetailsService，因为需要用它来读取用户信息
		SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
		smsAuthenticationProvider.setUserDetailsService(userDetailsService);
		// 最后一步，将这些组件加入到SpringSecurity安全框架中
		http.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsCodeAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class);
	}

}
