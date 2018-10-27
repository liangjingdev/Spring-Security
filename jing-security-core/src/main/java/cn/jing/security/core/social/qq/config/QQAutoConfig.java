/**
 * 
 */
package cn.jing.security.core.social.qq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import cn.jing.security.core.properties.QQProperties;
import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.social.qq.connet.QQConnectionFactory;

/**
 * function:构建QQ的ConnectionFactory连接工厂
 * 
 * @author liangjing
 * @ConditionalOnProperty(prefix = "jing.security.social.qq", name =
 *                               "app-id")表示指定的配置属性要有一个明确的值，即jing.security.social.qq.app-id要有一个明确的值，如果没有该属性或者该属性没有值的话，那么就不用去创建ConnectionFactory连接工厂。
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "jing.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}

}
