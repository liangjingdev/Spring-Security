package cn.jing.security.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import cn.jing.security.core.properties.SecurityConstants;

/**
 * function:与表单登录相关的安全配置
 * 
 * @author liangjing
 */
@Component
public class FormAuthenticationConfig {

	@Autowired
	protected AuthenticationSuccessHandler jingAuthenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler jingAuthenticationFailureHandler;

	public void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
				.loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
				.successHandler(jingAuthenticationSuccessHandler).failureHandler(jingAuthenticationFailureHandler);
	}

}
