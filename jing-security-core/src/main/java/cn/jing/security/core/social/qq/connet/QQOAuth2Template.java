/**
 * 
 */
package cn.jing.security.core.social.qq.connet;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * function:替换掉默认的OAuth2Template类，然后根据QQ服务提供商制定的规则进行相对应的修改
 * 
 * @author liangjing
 *
 */
public class QQOAuth2Template extends OAuth2Template {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
		// 注意useParametersForClientAuthentication的值默认是false的，我们需要将它设置为true，这样的话在exchangeForAccess方法中才能够携带获取令牌的所必需的5个参数信息去发起请求来获取令牌信息
		setUseParametersForClientAuthentication(true);
	}

	/**
	 * function:发送post请求获取令牌信息，并将获取到的令牌信息封装到AccessGrant类对象中
	 */
	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);

		logger.info("获取accessToke的响应：" + responseStr);

		String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");

		String accessToken = StringUtils.substringAfterLast(items[0], "=");
		Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
		String refreshToken = StringUtils.substringAfterLast(items[2], "=");

		return new AccessGrant(accessToken, null, refreshToken, expiresIn);
	}

	/**
	 * function:RestTemplate是不能够处理content-type是text/html的这种响应的，因为在创建RestTemplate的时候，里面去加HttpMessageConverter的时候并没有加上处理text/html这种content-type的MessageConverter。
	 * HttpMessageConverter这个东西就是用来处理响应的。
	 */
	@Override
	protected RestTemplate createRestTemplate() {
		// 先拿到父类创建好的RestTemplate
		RestTemplate restTemplate = super.createRestTemplate();
		// 然后在父类创建好的RestTemplate的基础上再添加一个HttoMessageConverter
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}

}
