/**
 * 
 */
package cn.jing.security.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import cn.jing.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import cn.jing.security.core.authentication.FormAuthenticationConfig;
import cn.jing.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import cn.jing.security.core.authorize.AuthorizeConfigManager;
import cn.jing.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * function:资源服务器安全配置类，通过继承ResourceServerConfigurerAdapter类并重写configure方法去添加一些自定义的配置
 * 
 * @author liangjing
 *
 */
@Configuration
@EnableResourceServer
public class JingResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	protected AuthenticationSuccessHandler jingAuthenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler jingAuthenticationFailureHandler;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SpringSocialConfigurer jingSocialSecurityConfig;

	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;

	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;

	/**
	 * function:配置资源的访问规则
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {

		formAuthenticationConfig.configure(http);

		http.apply(validateCodeSecurityConfig).and().apply(smsCodeAuthenticationSecurityConfig).and()
				.apply(jingSocialSecurityConfig).and().apply(openIdAuthenticationSecurityConfig).and().csrf().disable();

		authorizeConfigManager.config(http.authorizeRequests());
	}

}