package cn.jing.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * function:授权配置管理器接口
 * 
 * 用于收集系统中所有的AuthorizeConfigProvider并加载其授权配置
 * 
 * @author liangjing
 *
 */
public interface AuthorizeConfigManager {

	/**
	 * function:加载系统中所有的AuthorizeConfigProvider的授权配置
	 * 
	 * @param config
	 */
	void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
