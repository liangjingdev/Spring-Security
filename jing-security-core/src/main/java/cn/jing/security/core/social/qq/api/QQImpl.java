/**
 * 
 */
package cn.jing.security.core.social.qq.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * function:api接口的实现类
 * 
 * @author liangjing
 *
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

	// 获取QQ用户openId的url
	private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
	// 获取QQ用户基本信息的url（需要传递三个参数，但是这里的access_token这个参数是交由父类AbstractOAuth2ApiBinding来进行处理的，它会自动帮我们补上去）
	private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

	private String appId;

	private String openId;

	private ObjectMapper objectMapper = new ObjectMapper();

	public QQImpl(String accessToken, String appId) {
		// 调用父类的构造函数，将accessToken以及相对应的Token策略传递上去
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

		this.appId = appId;

		String url = String.format(URL_GET_OPENID, accessToken);
		// getRestTemplate()方法是父类提供的，会返回一个RestTemplate对象
		String result = getRestTemplate().getForObject(url, String.class);

		System.out.println(result);
		// 将openId从返回来的json字符串中给截取出来
		this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}

	@Override
	public QQUserInfo getUserInfo() {

		String url = String.format(URL_GET_USERINFO, appId, openId);
		String result = getRestTemplate().getForObject(url, String.class);

		System.out.println(result);

		QQUserInfo userInfo = null;
		try {
			userInfo = objectMapper.readValue(result, QQUserInfo.class);
			userInfo.setOpenId(openId);
			return userInfo;
		} catch (Exception e) {
			throw new RuntimeException("获取用户信息失败", e);
		}
	}

}
