/**
 * 
 */
package cn.jing.security.core.social.qq.connet;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import cn.jing.security.core.social.qq.api.QQ;

/**
 * function:该工厂类负责创建Connection接口实现类对象
 * 
 * @author liangjing
 *
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

	public QQConnectionFactory(String providerId, String appId, String appSecret) {
		super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
	}

}
