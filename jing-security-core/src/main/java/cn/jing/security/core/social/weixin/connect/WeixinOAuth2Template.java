/**
 * 
 */
package cn.jing.security.core.social.weixin.connect;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * function:完成微信的OAuth2认证流程的模板类。国内厂商实现的OAuth2每个都不同,
 * spring默认提供的OAuth2Template适应不了，只能针对每个厂商自己微调。
 * 该类覆盖了父类的几个方法，分别是：exchangeForAccess、refreshAccess以及buildAuthenticateUrl。
 * 这些方法的共同点都是要去构建发出去的请求，往请求上去挂参数，挂OAuth协议规定必须携带的那些参数，
 * 但是OAuth协议规定必须携带的那些参数跟微信实现的参数的名字不一样，微信没有按照OAuth协议的规定来做。比如说，在换取accessToken的时候，标准的OAuth协议规定的所必需携带参数中的client_id、client_secret与微信所实现的参数的名字不一致。
 * 在微信中，appid表示client_id，secret表示client_secret，其它都一致。又由于SpringSocial是按照OAuth规范标准来编写的，像这些参数的名字，都是直接在代码里用字符串写死的，它没有接口说你可以把这些参数的名字给修改掉，那么现在我们要改
 * 变发出去的参数的名字，我们只能够自己去拼请求的串，然后把参数的名字按照微信的文档改成相应的微信所规定的名字。
 * 
 * @author liangjing
 *
 */
public class WeixinOAuth2Template extends OAuth2Template {

	private String clientId;

	private String clientSecret;

	private String accessTokenUrl;

	private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

	private Logger logger = LoggerFactory.getLogger(getClass());

	public WeixinOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
		// 将父类的useParametersForClientAuthentication属性设置为true，表示在发请求的时候会自动将clientId以及clientSecret设置到url上
		setUseParametersForClientAuthentication(true);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessTokenUrl = accessTokenUrl;
	}

	@Override
	public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri,
			MultiValueMap<String, String> parameters) {

		StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);

		accessTokenRequestUrl.append("?appid=" + clientId);
		accessTokenRequestUrl.append("&secret=" + clientSecret);
		accessTokenRequestUrl.append("&code=" + authorizationCode);
		accessTokenRequestUrl.append("&grant_type=authorization_code");
		accessTokenRequestUrl.append("&redirect_uri=" + redirectUri);

		return getAccessToken(accessTokenRequestUrl);
	}

	public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {

		StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);

		refreshTokenUrl.append("?appid=" + clientId);
		refreshTokenUrl.append("&grant_type=refresh_token");
		refreshTokenUrl.append("&refresh_token=" + refreshToken);

		return getAccessToken(refreshTokenUrl);
	}

	@SuppressWarnings("unchecked")
	private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl) {

		logger.info("获取access_token, 请求URL: " + accessTokenRequestUrl.toString());

		String response = getRestTemplate().getForObject(accessTokenRequestUrl.toString(), String.class);

		logger.info("获取access_token, 响应内容: " + response);

		Map<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(response, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回错误码时直接返回空
		if (StringUtils.isNotBlank(MapUtils.getString(result, "errcode"))) {
			String errcode = MapUtils.getString(result, "errcode");
			String errmsg = MapUtils.getString(result, "errmsg");
			throw new RuntimeException("获取access token失败, errcode:" + errcode + ", errmsg:" + errmsg);
		}

		WeixinAccessGrant accessToken = new WeixinAccessGrant(MapUtils.getString(result, "access_token"),
				MapUtils.getString(result, "scope"), MapUtils.getString(result, "refresh_token"),
				MapUtils.getLong(result, "expires_in"));

		// 除了返回以上四个字段之外，还有openid字段
		accessToken.setOpenId(MapUtils.getString(result, "openid"));

		return accessToken;
	}

	/**
	 * function:构建获取授权码的请求。也就是引导用户跳转到微信的地址。
	 */
	public String buildAuthenticateUrl(OAuth2Parameters parameters) {
		String url = super.buildAuthenticateUrl(parameters);
		url = url + "&appid=" + clientId + "&scope=snsapi_login";
		return url;
	}

	public String buildAuthorizeUrl(OAuth2Parameters parameters) {
		return buildAuthenticateUrl(parameters);
	}

	/**
	 * function:微信返回的contentType是html/text，添加相应的HttpMessageConverter来处理。
	 */
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = super.createRestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}

}
