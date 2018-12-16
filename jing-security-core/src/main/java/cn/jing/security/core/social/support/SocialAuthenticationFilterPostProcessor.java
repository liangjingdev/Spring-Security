/**
 * 
 */
package cn.jing.security.core.social.support;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * function:SocialAuthenticationFilter后处理器，用于在不同环境下个性化社交登录的配置
 * 
 * @author liangjing
 *
 */
public interface SocialAuthenticationFilterPostProcessor {

	/**
	 * @param socialAuthenticationFilter
	 */
	void process(SocialAuthenticationFilter socialAuthenticationFilter);

}
