/**
 * 
 */
package cn.jing.security.core.social.weixin.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

import cn.jing.security.core.social.weixin.api.Weixin;

/**
 * function:微信连接工厂
 * 
 * @author liangjing
 *
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

	/**
	 * @param appId
	 * @param appSecret
	 */
	public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
		super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
	}

	/**
	 * function:由于微信的openId是和令牌一起返回的，所以在这里直接根据令牌信息设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
	 */
	@Override
	protected String extractProviderUserId(AccessGrant accessGrant) {
		if (accessGrant instanceof WeixinAccessGrant) {
			return ((WeixinAccessGrant) accessGrant).getOpenId();
		}
		return null;
	}

	/*
	 * function:覆盖掉父类中的两个Connection对象创建的方法。在默认的实现里面，也就是在创建Connection对象的时候，前面传的参数都一样，
	 * 不一样的是最后传的参数。 默认的实现里，在父类中，getApiAdapter方法里返回的ApiAdapter接口实现类对象都是同一个实例，
	 * 也就是说所有的OAuth2Connection拿到的都是同一个ApiAdapter接口实现类对象。
	 * 而在微信的实现里，是根据返回来的accessGrant里包含的那个openId，
	 * 针对每一个OAuth2Connection都new了一个新的WeixinAdapter，这里就是跟QQ最大不同的地方，
	 * WeixinAdapter是一个多实例的对象。 因为不同的微信用户，openId也就不一样。
	 */
	public Connection<Weixin> createConnection(AccessGrant accessGrant) {
		return new OAuth2Connection<Weixin>(getProviderId(), extractProviderUserId(accessGrant),
				accessGrant.getAccessToken(), accessGrant.getRefreshToken(), accessGrant.getExpireTime(),
				getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
	}

	public Connection<Weixin> createConnection(ConnectionData data) {
		return new OAuth2Connection<Weixin>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
	}

	private ApiAdapter<Weixin> getApiAdapter(String providerUserId) {
		return new WeixinAdapter(providerUserId);
	}

	private OAuth2ServiceProvider<Weixin> getOAuth2ServiceProvider() {
		return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
	}

}
