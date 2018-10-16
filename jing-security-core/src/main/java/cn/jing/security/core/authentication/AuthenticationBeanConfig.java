package cn.jing.security.core.authentication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * 
 * function:与认证相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全模块中默认的配置。
 * 
 * @author liangjing
 *
 */
@Configuration
public class AuthenticationBeanConfig {

	/**
	 * function:默认的密码处理器
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * function:默认的UserDetailsService接口的实现类(默认的认证器)
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public UserDetailsService userDetailsService() {
		return new DefaultUserDetailsService();
	}

	/**
	 * function:默认的SocialUserDetailsService接口的实现类默认的认证器
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SocialUserDetailsService.class)
	public SocialUserDetailsService socialUserDetailsService() {
		return new DefaultSocialUserDetailsService();
	}

}
