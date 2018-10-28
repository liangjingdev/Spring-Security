/**
 * 
 */
package cn.jing.security.core.authorize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * function:默认的授权配置管理器
 * 
 * @author liangjing
 *
 */
@Component
public class JingAuthorizeConfigManager implements AuthorizeConfigManager {

	// 将系统中所有的AuthorizeConfigProvider收集起来
	@Autowired
	private List<AuthorizeConfigProvider> authorizeConfigProviders;

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		// 系统默认情况下没有配置"anyRequest().authenticated()"
		boolean existAnyRequestConfig = false;
		// 配置了"anyRequest().authenticated()"的AuthorizeConfigProvider接口实现类的类名
		String existAnyRequestConfigName = null;

		for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
			// currentIsAnyRequestConfig值为true则说明当前这个AuthorizeConfigProvider接口实现类有配置"anyRequest().authenticated()"
			boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(config);
			if (existAnyRequestConfig && currentIsAnyRequestConfig) {
				throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + ","
						+ authorizeConfigProvider.getClass().getSimpleName());
			} else if (currentIsAnyRequestConfig) {
				// existAnyRequestConfig值为true则表示系统中目前已经有配置"anyRequest().authenticated()"了，
				// 若下一个AuthorizeConfigProvider接口实现类也有配置"anyRequest().authenticated()的话，那么系统就会抛出异常
				existAnyRequestConfig = true;
				existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
			}
		}

		// 如果所有的AuthorizeConfigProvider接口实现类都没有配置"anyRequest().authenticated()"的话，那么系统就自动添加该配置
		if (!existAnyRequestConfig) {
			config.anyRequest().authenticated();
		}
	}

}
