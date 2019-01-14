package cn.jing.security.app.social;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import cn.jing.security.core.properties.SecurityConstants;
import cn.jing.security.core.social.support.JingSpringSocialConfigurer;

/**
 * function:由于在Core模块中进行社交登录配置时(SocialConfig)，所设置的signupUrl是针对浏览器环境下的，那么现在在app环境下，社交登录时，如果需要用户进行注册，则需要去修改之前配置的signupUrl。
 * 实现BeanPostProcessor接口的类的作用是：在Spring容器里所有的Bean初始化之前和初始化完成之后，都要经过以下两个方法。
 * 
 * @author liangjing
 * 
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

	// 在初始化之前什么都不用做，原样返回即可
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	// 在app环境下当cn.jing.security.core.social包中声明的SpringSocialConfigurer这个Bean初始化好以后，把
	// 原来设置的signupUrl改掉，就这么简单
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// 判断如果bean的名字是jingSocialSecurityConfig的话，那么就进行处理，否则直接将bean返回即可
		if (StringUtils.equals(beanName, "jingSocialSecurityConfig")) {
			// 类型转换
			JingSpringSocialConfigurer config = (JingSpringSocialConfigurer) bean;
			config.signupUrl(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL);
			return config;
		}
		return bean;
	}

}
