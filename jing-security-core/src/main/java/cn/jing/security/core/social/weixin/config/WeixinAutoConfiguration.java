/**
 * 
 */
package cn.jing.security.core.social.weixin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.properties.WeixinProperties;
import cn.jing.security.core.social.view.JingConnectView;
import cn.jing.security.core.social.weixin.connect.WeixinConnectionFactory;

/**
 * function:微信登录配置
 * 
 * @author liangjing
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "jing.security.social.weixin", name = "app-id")
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * function:构建ConnectionFactory
	 */
	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
		return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
				weixinConfig.getAppSecret());
	}

	/**
	 * function:绑定/解绑成功视图
	 * 
	 * @return
	 */
	@Bean({ "connect/weixinConnect", "connect/weixinConnected" })
	@ConditionalOnMissingBean(name = "weixinConnectedView")
	public View weixinConnectedView() {
		return new JingConnectView();
	}

}
