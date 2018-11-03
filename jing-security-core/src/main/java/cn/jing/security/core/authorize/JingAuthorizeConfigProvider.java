/**
 * 
 */
package cn.jing.security.core.authorize;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import cn.jing.security.core.properties.SecurityConstants;
import cn.jing.security.core.properties.SecurityProperties;

/**
 * function:核心模块的授权配置提供器，安全模块涉及到的url的授权配置在这里
 * 
 * @author liangjing
 *
 */
@Component
@Order(Integer.MIN_VALUE) // 该注解的作用：若同时有几个对象都是AuthorizeConfigProvider接口的实现类，那么值越低，优先级越高，数据越先被读入
public class JingAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE,
				SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_OPENID,
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
				securityProperties.getBrowser().getSignInPage(), securityProperties.getBrowser().getSignUpUrl(),
				securityProperties.getBrowser().getSession().getSessionInvalidUrl()).permitAll();

		if (StringUtils.isNotBlank(securityProperties.getBrowser().getSignOutUrl())) {
			config.antMatchers(securityProperties.getBrowser().getSignOutUrl()).permitAll();
		}
		return false;
	}

}
